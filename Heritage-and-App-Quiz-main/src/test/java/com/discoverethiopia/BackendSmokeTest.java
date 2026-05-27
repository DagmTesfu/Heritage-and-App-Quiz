package com.discoverethiopia;

import com.discoverethiopia.model.ChurchSite;
import com.discoverethiopia.model.HeritageSite;
import com.discoverethiopia.model.Question;
import com.discoverethiopia.model.QuizAttempt;
import com.discoverethiopia.util.PasswordHasher;

import java.time.LocalDateTime;

public class BackendSmokeTest {
    public static void main(String[] args) {
        HeritageSite lalibela = new ChurchSite(
                1,
                "Lalibela Rock Churches",
                "Amhara",
                "Eleven churches carved from rock.",
                "Built during the Zagwe dynasty; Some churches are connected by tunnels",
                "images/lalibela.jpg",
                1);

        Question question = new Question(
                1,
                1,
                "How many rock-hewn churches are in Lalibela?",
                "5",
                "11",
                "13",
                "7",
                'B',
                "Lalibela has eleven rock-hewn churches.");

        QuizAttempt attempt = new QuizAttempt(1, 2, 1, 1, 1, 100, LocalDateTime.now());

        if (!"church".equals(lalibela.getType())) {
            throw new IllegalStateException("HeritageSite polymorphism failed.");
        }
        if (!question.isCorrect('B')) {
            throw new IllegalStateException("Question answer checking failed.");
        }
        if (!attempt.getMessage().contains("Great")) {
            throw new IllegalStateException("QuizAttempt result message failed.");
        }
        if (!PasswordHasher.hash("admin123")
                .equals("240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9")) {
            throw new IllegalStateException("Password hashing failed.");
        }

        System.out.println("Person A backend smoke test passed.");
    }
}
