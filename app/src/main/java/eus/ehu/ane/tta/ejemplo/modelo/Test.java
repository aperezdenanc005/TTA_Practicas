package eus.ehu.ane.tta.ejemplo.modelo;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ane on 9/01/18.
 */

public class Test
{
    private String wording;
    private List<Choice> choices=new ArrayList<>();

    public Test()
    {

    }

    public Test(String wording, List<Choice> choices)
    {
        this.wording=wording;
        this.choices=choices;
    }

    public String getWording() {
        return wording;
    }

    public void setWording(String wording) {
        this.wording = wording;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public static class Choice implements Serializable
    {
        private int id;
        private String advise;
        private String answer;
        private boolean correct;
        private String mime;

        public Choice()
        {

        }

        public Choice(int id, String advise, String answer, boolean correct, String mime)
        {
            this.id=id;
            this.advise=advise;
            this.answer=answer;
            this.correct=correct;
            this.mime=mime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAdvise() {
            return advise;
        }

        public void setAdvise(String advise) {
            this.advise = advise;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public boolean isCorrect() {
            return correct;
        }

        public void setCorrect(boolean correct) {
            this.correct = correct;
        }

        public String getMime() {
            return mime;
        }

        public void setMime(String mime) {
            this.mime = mime;
        }
    }
}
