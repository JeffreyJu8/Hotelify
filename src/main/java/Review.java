import org.apache.catalina.User;

public class Review {
	private User user;
	private String fileName;
    private int rating;
    private String textField;
    
    public void setFileName(String f) {
        this.fileName = f;
    }
    
    public void setRating(int rating) {
        this.rating = rating;
    }
    
    public void setTextField(String text) {
        this.textField = text;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public String getRatingStr() {
        return Integer.toString(rating);
    }
    
    public String getTextField() {
        return textField;
    }

    
    
    
}