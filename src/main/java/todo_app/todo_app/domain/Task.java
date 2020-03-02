package todo_app.todo_app.domain;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.format.annotation.DateTimeFormat;


import lombok.AllArgsConstructor;
import lombok.Data;

@Entity //RDBで管理されているレコードのデータを格納する
@Table(name = "task") //@tableアボテーションをつけることで、name属性にEntityクラスマップングするテーブル名を指定できる
@Data 
@AllArgsConstructor //AllArgsControllerは全てのフィールドを引数に持つコンストラクタを用意するもの
public class Task{

    @Id
    @GeneratedValue(strategy=GenerationType.Auto) //GeneratedValueを使って主キーにユニークな値を自動で生成し、@Idを持つフィルドに適用できます。
    Integer id;

    String subject;

    @JsonProperty("dead-line")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    LocalDate deadLine;

    @JsonProperty("done") //@JsonProperty アノテーションを使って JSON をパースしてオブジェクト化する
    Boolean hasDone;

    public Task(){
    }

    public Task(String subject, LocalDate deadLine, Boolean hasDone){
        this.subject = subject;
        this.deadLine = deadLine;
        this.hasDone = hasDone;
    }

}