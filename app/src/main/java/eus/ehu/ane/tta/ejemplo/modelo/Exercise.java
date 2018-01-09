package eus.ehu.ane.tta.ejemplo.modelo;

/**
 * Created by ane on 9/01/18.
 */

public class Exercise
{
    private String wording;
    private LessonBean lessonBean;

    public Exercise()
    {

    }

    public Exercise(String wording, LessonBean lessonBean)
    {
        this.wording=wording;
        this.lessonBean=lessonBean;
    }

    public String getWording() {
        return wording;
    }

    public void setWording(String wording) {
        this.wording = wording;
    }

    public LessonBean getLessonBean() {
        return lessonBean;
    }

    public void setLessonBean(LessonBean lessonBean) {
        this.lessonBean = lessonBean;
    }

    public class LessonBean
    {
        private int number;
        private String title;

        public LessonBean()
        {

        }

        public LessonBean(int number, String title)
        {
            this.number=number;
            this.title=title;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
