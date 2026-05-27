# Discover Ethiopia - Heritage & Quiz App

Desktop app built with Java Swing, MySQL, JDBC, and OOP.

## Roles

- Person A: backend logic, OOP model classes, DAO layer, password hashing, MySQL schema, and seed data.
- Person B: Swing screens, login/register flow, user dashboard, admin dashboard, images, quiz screens, and forms.

## What The App Does

- Users can register and log in.
- Users can explore heritage sites with descriptions, facts, and images.
- Users can take quizzes for each heritage site.
- Quiz scores and wrong answers are saved.
- Users can suggest missing heritage sites.
- Admins can add/delete heritage sites, add/delete quiz questions, and approve/reject suggestions.

## Folder Structure

- `src/main/java/com/discoverethiopia/model` - OOP domain classes.
- `src/main/java/com/discoverethiopia/dao` - JDBC database access.
- `src/main/java/com/discoverethiopia/db` - database connection config.
- `src/main/java/com/discoverethiopia/service` - quiz provider interface and implementation.
- `src/main/java/com/discoverethiopia/ui` - Swing user interface.
- `src/main/java/com/discoverethiopia/util` - password hashing helper.
- `src/test/java/com/discoverethiopia/BackendSmokeTest.java` - simple backend compile/run check.
- `sql/schema.sql` - creates the database tables.
- `sql/seed_data.sql` - inserts demo users, heritage sites, and quiz questions.
- `images` - app images.

## MySQL Setup

Open MySQL and run:

```sql
SOURCE C:/Users/Dagmawi Tesfu/Desktop/Heritage-and-App-Quiz-main/Heritage-and-App-Quiz-main/sql/schema.sql;
SOURCE C:/Users/Dagmawi Tesfu/Desktop/Heritage-and-App-Quiz-main/Heritage-and-App-Quiz-main/sql/seed_data.sql;
```

Default database settings:

```text
URL: jdbc:mysql://localhost:3306/myprojectdb
USER: root
PASSWORD: empty by default
```

Override them with environment variables:

```text
DB_URL
DB_USER
DB_PASSWORD
```

Or Java system properties:

```powershell
java -Ddb.user=root -Ddb.password=yourpassword ...
```

## Demo Accounts

```text
Admin email: admin@discoverethiopia.local
Admin password: admin123

User email: demo@discoverethiopia.local
User password: user123
```

## Compile Check

From this folder:

```powershell
javac -d out (Get-ChildItem -Recurse src/main/java/*.java,src/test/java/*.java).FullName
java -cp out com.discoverethiopia.BackendSmokeTest
```

Expected output:

```text
Person A backend smoke test passed.
```
