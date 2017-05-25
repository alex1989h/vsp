package spring;

public class Test {

    private final long id;
    private final String content;
	private final String frage;

    public Test(long id, String content, String frage) {
        this.id = id;
        this.content = content;
		this.frage = frage;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
	
	public String getFrage() {
        return frage;
    }
}