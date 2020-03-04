package todo_app.todo_app.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor //引数がないコンストラクタを用意する
@AllArgsConstructor 
//引数のあるコンストラクタを用意する
public class TaskForm{

    String subject; //登録フォームのタスクを入れるフィールド

    @Date
    TimeFormat(pattern = "yyyy-MM-dd")
    LocalDate deadLine; //登録フォームの期日を入れるフィールド

    Boolean hasDone; //タスクが完了したのかの状態を入れる論理型のフィールド

    Boolean isNewTask; //既に登録されているタスクかを判別する論理型のフィールド
}
