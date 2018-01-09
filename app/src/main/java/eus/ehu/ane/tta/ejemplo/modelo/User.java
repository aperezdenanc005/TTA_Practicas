package eus.ehu.ane.tta.ejemplo.modelo;

/**
 * Created by ane on 9/01/18.
 */

public class User
{
    private int id;
    private String user;
    private int lessonNumber;
    private String lessonTitle;
    private int nextTest;
    private int nextExercise;

    public User()
    {

    }

    public User(int id, String user, int lessonNumber, String lessonTitle, int nextTest, int nextExercise)
    {
        this.id=id;
        this.user=user;
        this.lessonNumber=lessonNumber;
        this.lessonTitle=lessonTitle;
        this.nextTest=nextTest;
        this.nextExercise=nextExercise;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(int lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
    }

    public int getNextTest() {
        return nextTest;
    }

    public void setNextTest(int nextTest) {
        this.nextTest = nextTest;
    }

    public int getNextExercise() {
        return nextExercise;
    }

    public void setNextExercise(int nextExercise) {
        this.nextExercise = nextExercise;
    }
}
