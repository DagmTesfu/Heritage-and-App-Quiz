package com.discoverethiopia.ui.components;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class CustomTextField extends JTextField {

    private final String hint;

    private final Color normalBorderColor =
            new Color(200, 200, 200);

    private final Color focusBorderColor =
            new Color(120, 0, 180);

    public CustomTextField(String hint, int columns) {

        super(hint, columns);

        this.hint = hint;

        // TEXT STYLE
        setFont(new Font("Segoe UI", Font.PLAIN, 15));

        setForeground(Color.GRAY);

        setBackground(Color.WHITE);

        setCaretColor(Color.BLACK);

        // SIZE
        setPreferredSize(new Dimension(250, 45));

        // PADDING
        setMargin(new Insets(5, 12, 5, 12));

        // ROUNDED BORDER
        setBorder(new CompoundBorder(

                new LineBorder(normalBorderColor, 2, true),

                new EmptyBorder(5, 10, 5, 10)
        ));

        // FOCUS EFFECTS
        addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {

                // REMOVE HINT
                if (getText().equals(hint)) {

                    setText("");

                    setForeground(Color.BLACK);
                }

                // GLOW BORDER
                setBorder(new CompoundBorder(

                        new LineBorder(focusBorderColor, 2, true),

                        new EmptyBorder(5, 10, 5, 10)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {

                // RETURN HINT
                if (getText().isEmpty()) {

                    setText(hint);

                    setForeground(Color.GRAY);
                }

                // NORMAL BORDER
                setBorder(new CompoundBorder(

                        new LineBorder(normalBorderColor, 2, true),

                        new EmptyBorder(5, 10, 5, 10)
                ));
            }
        });
    }
}