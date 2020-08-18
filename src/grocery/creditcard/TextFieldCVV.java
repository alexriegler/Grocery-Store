package grocery.creditcard;

import javafx.scene.control.TextField;

/**
 * @author Martijn
 */
public class TextFieldCVV extends TextField {
    private int maxlength;
    public TextFieldCVV() {
        this.maxlength = 3;
    }
    public void setMaxlength(int maxlength) {
        this.maxlength = maxlength;
    }
    @Override
    public void replaceText(int start, int end, String text) {
        // Delete or backspace user input.
        if (text.equals("")) {
            super.replaceText(start, end, text);
        } else if (getText().length() < maxlength) {
            if (validate(text))
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        // Delete or backspace user input.
        if (text.equals("")) {
            super.replaceSelection(text);
        } else if (getText().length() < maxlength) {
            // Add characters, but don't exceed maxlength.
            if (text.length() > maxlength - getText().length()) {
                text = text.substring(0, maxlength- getText().length());
            }
            super.replaceSelection(text);
        }

    }

    private boolean validate(String text)
    {
        return text.matches("[0-9]*");
    }

}