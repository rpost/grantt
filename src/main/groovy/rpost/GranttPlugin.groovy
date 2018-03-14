package rpost

import groovy.json.JsonOutput
import groovy.text.StreamingTemplateEngine
import groovy.text.Template
import groovy.transform.Canonical
import org.codehaus.groovy.runtime.IOGroovyMethods
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.concurrent.ConcurrentHashMap

class GranttPlugin implements Plugin<Project> {

    private static final String D3JS_RESOURCE_PATH = "/META-INF/resources/webjars/d3js/4.10.2/d3.min.js"
    private static final String CHART_TEMPLATE_RESOURCE_PATH = "/gantt.html"

    private Map<String, TaskStats> taskTimings = new ConcurrentHashMap<>()

    void apply(Project project) {
        project.gradle.taskGraph.whenReady {
            it.allTasks.each { task -> addTimeRecording(task)}
        }

        project.gradle.buildFinished {
            File buildDir = project.buildDir
            if (buildDir.exists()) {
                Path chartPath = buildDir.toPath().resolve("gantt.html")
                createChartFile(
                    chartPath,
                    getExecutionTimesAsJson(),
                    project.name
                )
            }
        }
    }

    private void createChartFile(Path chartPath, String json, String projectName) {
        Template template = new StreamingTemplateEngine().createTemplate(readResource(CHART_TEMPLATE_RESOURCE_PATH))
        Writable templateWithData = template.make([
            d3js       : readResource(D3JS_RESOURCE_PATH),
            data       : json,
            projectName: projectName,
        ])
        IOGroovyMethods.withCloseable(
            Files.newBufferedWriter(
                chartPath,
                StandardCharsets.UTF_8,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.CREATE
            ),
            { templateWithData.writeTo(it) }
        )
    }

    private String getExecutionTimesAsJson() {
        return JsonOutput.prettyPrint(
            JsonOutput.toJson(
                taskTimings.entrySet().collect(
                    { [
                        name: it.key,
                        start: it.value.startTimeMillis,
                        end: it.value.endTimeMillis,
                        type: it.value.taskType
                    ] }
                )
            )
        )
    }

    private String readResource(String path) {
        return this.getClass()
            .getResource(path)
            .withInputStream {
                is -> return is.getText(StandardCharsets.UTF_8.name())
            }
    }

    void addTimeRecording(Task task) {
        task.doFirst {
            TaskStats timing = new TaskStats(startTimeMillis: System.currentTimeMillis(), taskType: it.class.getSimpleName())
            taskTimings.put(path, timing)
        }
        task.doLast {
            taskTimings.get(path).setEndTimeMillis(System.currentTimeMillis())
        }
    }

    @Canonical
    class TaskStats {
        Long startTimeMillis
        Long endTimeMillis
        String taskType
    }
}