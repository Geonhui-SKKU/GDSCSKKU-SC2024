package gdscskku.teletect.plugin.sms;
import java.util.List;

public class SendSMSMessage {

    private List<String> numbers;
    private String text;

    public SendSMSMessage(List<String> numbers, String text) {
        this.numbers = numbers;
        this.text = text;
    }

    public String getFirstNumber() {
        if (this.numbers != null && this.numbers.size() >= 1) {
            return this.numbers.get(0);
        }
        return null;

    }

    public List<String> getNumbers() {
        return this.numbers;
    }

    public String getText() {
        return text;
    }

    public String getJoinedNumbers(String separator) {
        StringBuilder joined = new StringBuilder();
        for (int i = 0; i < this.numbers.size(); i++) {
            if (i > 0) {
                joined.append(separator);
            }
            joined.append(this.numbers.get(i));
        }
        return joined.toString();
    }
}