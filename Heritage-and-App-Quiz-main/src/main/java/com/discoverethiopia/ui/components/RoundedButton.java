package com.discoverethiopia.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RoundedButton extends JButton {

    // NORMAL COLOR
    private final Color normalColor =
            new Color(98, 0, 140);

    // HOVER COLOR
    private final Color hoverColor =
            new Color(130, 0, 190);

    // GLOW BORDER COLOR
    private final Color glowColor =
            new Color(180, 100, 255);

    // ANIMATION SCALE
    private boolean hover = false;

    public RoundedButton(String text) {

        super(text);

        // BUTTON SETTINGS
        setFocusPainted(false);

        setContentAreaFilled(false);

        setBorderPainted(false);

        setForeground(Color.WHITE);

        setFont(new Font("Segoe UI", Font.BOLD, 16));

        setCursor(new Cursor(Cursor.HAND_CURSOR));

        setPreferredSize(new Dimension(180, 45));

        // HOVER EFFECTS
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                hover = true;

                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {

                hover = false;

                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        // SMOOTH GRAPHICS
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // GLOW EFFECT
        if (hover) {

            g2.setColor(new Color(
                    glowColor.getRed(),
                    glowColor.getGreen(),
                    glowColor.getBlue(),
                    90
            ));

            g2.fillRoundRect(
                    -3,
                    -3,
                    getWidth() + 6,
                    getHeight() + 6,
                    30,
                    30
            );
        }

        // BUTTON COLOR
        if (hover) {

            g2.setColor(hoverColor);

        } else {

            g2.setColor(normalColor);
        }

        // MAIN BUTTON
        g2.fillRoundRect(
                0,
                0,
                getWidth(),
                getHeight(),
                25,
                25
        );

        super.paintComponent(g2);

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // BORDER GLOW
        if (hover) {

            g2.setColor(glowColor);

            g2.setStroke(new BasicStroke(3));

        } else {

            g2.setColor(new Color(150, 80, 220));

            g2.setStroke(new BasicStroke(2));
        }

        // DRAW BORDER
        g2.drawRoundRect(
                1,
                1,
                getWidth() - 3,
                getHeight() - 3,
                25,
                25
        );

        g2.dispose();
    }
}