package dungnt.ptit.jiraspringboot;

public class Constants {
    public enum ISSUE_TYPE{
        STORY("Story"),TASK("Task"),BUG("Bug"),EPIC("Epic"),
        SUBTASK("Subtask");
        private String code;

        public String getCode(){
            return this.code;
        }

        ISSUE_TYPE(String code){
            this.code = code;
        }
    }
}
