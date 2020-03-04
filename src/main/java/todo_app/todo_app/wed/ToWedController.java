package todo_app.todo_app.wed;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;

import ch.qos.logback.core.helpers.Transform;
import todo_app.todo_app.domain.Task;
import todo_app.todo_app.form.TaskForm;
import todo_app.todo_app.service.TodoService;

@Controller
public class ToWedController{

    private static final String TASKS = "tasks";

    private static final String REDIRECT_TO = "redirect:/" + TASKS; //finalはそれが最後のものであると表す

    @Autowired
    TodoService todoService;

    //タスクの全件取得
    @GetMapping(value = "/tasks") //@RequestMappingのGETリクエスト用のアノテーションが@GetMappingです。記述の省略と可読性の向上が目的です。
    public ModelAndView readAllTasks(){
        TaskForm form = createInitialForm(); //オブジェクト生成の初期メソッド呼び出し
        ModelAndView modelAndView = toTasksPage();  //readAllTasksメソッドを用いることでthymeleafで作成するhtmlの画面へ繊維を行う
        modelAndView.addObject("form", form);
        List<Task> tasks = todoService.findAllTasks(); //データベースからデータを取得
        modelAndView.addObject(TASKS, tasks);
        return modelAndView;
    }

    private ModelAndView toTasksPage(){
        return new ModelAndView(TASKS);
    }

    private TaskForm createInitialForm(){
        String formSubject = "";
        LocalDate formDeadLine = LocalDate.now();
        Boolean isNewTask = true;
        Boolean hasDone = false;
        return new TaskForm(formSubject, formDeadLine, isNewTask, hasDone);
    }

    //タスクを一件作成
    @PostMapping(value = "/tasks") //HTTPのpostメソットで/menbersのリクエストを受けると、createOneTaskメソッドへルーティング可能
    public ModelAndView createOneTask(@ModelAttribute TaskForm form){//@ModelAttributeをつけることで、登録フォームで入力したデータをTransformクラスのオブジェクトで受け取り可能
        createTaskFromFrom(form); //登録フォームで流れてきたデータからTaskクラスのオブジェクトに変換して、登録処理を行う
        return new ModelAndView(REDIRECT_TO);
    }

    private void createTaskFromFrom(TaskForm form){
        String subject = form.getSubject();
        LocalDate deadline = form.getDeadLine();
        Boolean hasDone = form.getHasDone();
        Task task = new Task(subject, deadline, hasDone);
        todoService.createTask(task);
    }

    // タスクを1件取得
    @GetMapping(value = "/task/{id}") // GetMappingのアノテーションのvalue属性にURLのパスを含めるidを指定する
    public ModelAndView readOneTask(@PathVariable Integer id) { // REST形式のURIからパラメータを取るのに利用します。URLに含まれた文字列をパラメータとして受け取るためのアノテーションです。
        Optional<TaskForm> form = readTaskFromId(id);
        if (!form.isPresent()) { // 引数で受け取ったidが登録されているのか確認して、登録されていない場合はreadAllTasksメソッドにリダイレクトされる
            return new ModelAndView(REDIRECT_TO);
        }
        ModelAndView modelAndView = toTasksPage();
        modelAndView.addObject("taskId", id); // データベースが管理しているタスクのIDをtaskIdという変数名でThymeleafにわたす.このIdは登録されたタスクを編集するのに必要となる.
        modelAndView.addObject("form", form.get()); // データベースから取得できたデータをformという変数名でThymeleafにわたす.
        List<Task> task = todoService.findAllTasks();
        modelAndView.addObject(TASKS, task);
        return modelAndView;
    }

    private Optional<TaskForm> readTaskFromId(Integer id) {
        Optional<Task> task = todoService.findOneTask(id);
        if (!task.isPresent()) { // Optionalは値をラップし、 その値がnullかもしれない ことを表現するクラス
            return Optional.ofNullable(null); // いずれもstaticメソッド、Optionalはpublicなコンストラクタを持たないのでOptionalオブジェクトを生成するのに用いる
        }
        String formSubject = task.get().getSubject();
        LocalDate formDeadLine = task.get().getDeadLine();
        Boolean hasDone = task.get().getHasDone();
        Boolean isNewTask = false;
        TaskForm form = new TaskForm(formSubject, formDeadLine, hasDone, isNewTask);
        return Optional.ofNullable(form);
    }

    //タスクを1件更新
    @PutMapping(value = "/task/{id}") //PutMappingのvalue属性にURLのパスを含めるidを指定する。id=1のとき、putのhttpメソッドでtask/1のリクエストを受けるとupdateOneTaskメソッドへルーティングを行う
    public ModelAndView updateOneTask(@PathVariable Integer id, @ModelAttribute TaskForm form){ //引数に@PathVariableアノテーションを指定し、変数名をidと揃えることでURLのパスからidの値を取得可能,登録データで入力したデータをTaskFormクラスのオブジェクトで受け取ることができる
        updateTask(id, form); //ThymeleafのtaskId変数経由で受け取ったidと登録フォームに入力されたデータを登録する.
        return null;
    }

    private void updateTask(Integer id, TaskForm form){
        String subject = form.getSubject();
        LocalDate deadline = form.getDeadLine();
        Boolean hasDone = form.getHasDone();
        Task task = new Task(id, subject, deadline, hasDone);
        todoService.updateTask(task);
    }

    //タスクを1件削除
    @DeleteMapping(value = "tasks/{id}")
    public ModelAndView deleteOneTask(@PathVariable Integer id){
        deleteTask(id); //idを利用してタスクがデータベースに登録されているのかを確認する。登録されている場合にタスクの削除を行い、readAllTasksメソッドへリダイレクトする
        return new ModelAndView(REDIRECT_TO);
    }

    private void deleteTask(Integer id){
        Optional<Task> task = todoService.findOneTask(id);
        if(task.isPresent()){
            todoService.deleteTask(id);
        }
    }


}