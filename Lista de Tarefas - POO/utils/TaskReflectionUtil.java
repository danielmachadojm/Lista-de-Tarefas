package todoapp.utils;

import todoapp.annotations.TaskProperty;
import todoapp.models.Task;

import java.lang.reflect.Field;

public class TaskReflectionUtil {

    public static void displayTaskProperties(Task task) {
        System.out.println("Propriedades da tarefa:");
        Field[] fields = task.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            TaskProperty annotation = field.getAnnotation(TaskProperty.class);
            try {
                String desc = annotation != null ? annotation.description() : "sem descrição";
                Object val = field.get(task);
                System.out.printf("%s = %s (%s)%n", field.getName(), val, desc);
            } catch (IllegalAccessException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    public static boolean validateRequiredFields(Task task) {
        for (Field field : task.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            TaskProperty annotation = field.getAnnotation(TaskProperty.class);
            if (annotation != null && annotation.required()) {
                try {
                    Object val = field.get(task);
                    if (val == null) return false;
                } catch (IllegalAccessException e) {
                    return false;
                }
            }
        }
        return true;
    }
}
