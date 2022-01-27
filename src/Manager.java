import java.util.HashMap;

public class Manager {
    HashMap<Integer,Task> taskMap =new HashMap<>();
    HashMap<Integer,Epic> epicMap = new HashMap<>();

    public void addTaskToMap(Integer id,Task task){ // Задача помещается в хранилищею
        taskMap.put(id,task);
    }

    public void addEpicToMap(Integer id,Epic epic){ // Эпик помещается в хранилище.
        epicMap.put(id,epic);
    }

    public void addSubTaskMap(SubTask subTask,Epic epic){ // Добавляет подзадачу к эпику.
        epic.subTasks.add(subTask);
    }

    public void setStatusProgress(Integer id){ // Устанавливает значение задачи "в процессе".
        taskMap.get(id).setTaskNew(false);
        taskMap.get(id).setDone(false);
        taskMap.get(id).setInProgress(true);

    }
    public void setStatusDone(Integer id){ // Устанавливает значение задачи "выполнено".
        taskMap.get(id).setTaskNew(false);
        taskMap.get(id).setDone(true);
        taskMap.get(id).setInProgress(false);

    }

    public void setStatusProgressSubTask(Integer id, SubTask subTask){ //Устанавливает значение подзадачи "в процессе"
                                                                      // , проверяет статус выполнения эпика.
        int progress =0;

        for (int i = 0; i <epicMap.get(id).subTasks.size(); i++) {
            if(epicMap.get(id).subTasks.get(i)==subTask){
                epicMap.get(id).subTasks.get(i).setInProgress(true);
                epicMap.get(id).subTasks.get(i).setDone(false);
                epicMap.get(id).subTasks.get(i).setTaskNew(false);
            }
        }
        for (int i = 0; i <epicMap.get(id).subTasks.size(); i++){
            if(epicMap.get(id).subTasks.get(i).isInProgress()==true){
                progress++;
            }
            if(progress>0){
                epicMap.get(id).setInProgress(true);
                epicMap.get(id).setTaskNew(false);
                epicMap.get(id).setDone(false);
            }
        }
    }
    public void setStatusDoneSubTask(Integer id, SubTask subTask){ // Устанавливает значение подзадачи "выполнено"
                                                                  // , проверяет статус выполнения эпика.
        int progress =0;
        for (int i = 0; i <epicMap.get(id).subTasks.size(); i++) {
            if(epicMap.get(id).subTasks.get(i)==subTask){
                epicMap.get(id).subTasks.get(i).setInProgress(false);
                epicMap.get(id).subTasks.get(i).setDone(true);
                epicMap.get(id).subTasks.get(i).setTaskNew(false);

            }

        }
        for (int i = 0; i <epicMap.get(id).subTasks.size(); i++){

            if(epicMap.get(id).subTasks.get(i).isDone()==true){
                progress++;
            }
            if(progress==epicMap.get(id).subTasks.size()){
                epicMap.get(id).setInProgress(false);
                epicMap.get(id).setTaskNew(false);
                epicMap.get(id).setDone(true);
            }
        }

    }
  public void removeEverythingCompletely(){ // Очищает хранилища.
        taskMap.clear();
        epicMap.clear();
  }
  public void printAllTasks(){ // Печать всех задач.
        if(!taskMap.isEmpty()){
            for (Integer integer : taskMap.keySet()) {
                System.out.println("Задача "+taskMap.get(integer).getNameTask()+". Суть "
                        +taskMap.get(integer).getTaskBody());
                if(taskMap.get(integer).inProgress)
                    System.out.println("Задача в процессе исполнения.");
                if(taskMap.get(integer).isTaskNew())
                    System.out.println("К задаче еще не приступили.");
                if(taskMap.get(integer).isDone())
                    System.out.println("Задача выполнена.");
            }
        }
        if(!epicMap.isEmpty()){
            for (Integer integer : epicMap.keySet()) {
                System.out.println("Задача "+epicMap.get(integer).getNameTask()+". Суть "
                        +epicMap.get(integer).getTaskBody());
                if(epicMap.get(integer).inProgress)
                    System.out.println("Задача в процессе исполнения.");
                if(epicMap.get(integer).isTaskNew())
                    System.out.println("К задаче еще не приступили.");
                if(epicMap.get(integer).isDone())
                    System.out.println("Задача выполнена.");
                if(epicMap.get(integer).subTasks.size()>0){
                    for (int i = 0; i <epicMap.get(integer).subTasks.size() ; i++) {
                        System.out.println("Подзадача "+(i+1)+" "+epicMap.get(integer).subTasks.get(i).nameTask
                                +". Суть "+epicMap.get(integer).subTasks.get(i).getTaskBody());
                        if(epicMap.get(integer).subTasks.get(i).inProgress)
                            System.out.println("Задача в процессе исполнения.");
                        if(epicMap.get(integer).subTasks.get(i).isTaskNew())
                            System.out.println("К задаче еще не приступили.");
                        if(epicMap.get(integer).subTasks.get(i).isDone())
                            System.out.println("Задача выполнена.");

                    }
                }

            }
        }
  }

}