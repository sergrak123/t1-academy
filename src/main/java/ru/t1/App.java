package ru.t1;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.t1.service.TaskService;

@ComponentScan
public class App 
{
    public static void main( String[] args )
    {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(App.class);

        TaskService taskService = context.getBean(TaskService.class);

        //@Before
        System.out.println(taskService.isContainTask("task1"));

        //@AfterReturning
        taskService.getTasks().forEach(System.out::println);

        //@Around
        taskService.addTasks();

        //@AfterThrowing
        taskService.addTask(" ");
    }
}
