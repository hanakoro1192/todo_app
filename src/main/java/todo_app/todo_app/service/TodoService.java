package todo_app.todo_app.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import todo_app.todo_app.domain.Task;
import todo_app.todo_app.repository.TodoRepository;

//主にTodoRepositotyにエラーが多く存在する
//リポジトリを用いてデータ操作する実装
@Service
public class TodoService{
    @Autowired
    TodoRepository todoRepository; 

    @Transactional(readOnly=true) //@Transactional」はメソッドにつけるだけで自動的にトランザクションを行い、標準でRuntimeExceptionが発生した場合にロールバックを行ってくれる便利なもの readOnly=trueとすると登録更新が出来ないトランザクションになる
    public List<Task> findAllTasks(){
        List<Task> allTasks = todoRepository.findAll();
        allTasks.sort(Comparator.comparing(Task::getDeadLine));
        return allTasks;
    }

    @Transactional(readOnly=false)
    public Task  createTask(Task task){
        return todoRepository.save(task);
    }

    @Transactional(readOnly=false) //Optionalは値をラップし、 その値がnullかもしれない ことを表現するクラス
    public Optional<Task> findOneTask(Integer id){
        return todoRepository.findById(id);
    }

    @Transactional(readOnly=false)
    public Task updateTask(Task task){
        return todoRepository.save(task);
    }

    @Transactional(readOnly=false)
    public void deleteTask(Integer id){
        todoRepository.deleteById(id);
    }
    
}