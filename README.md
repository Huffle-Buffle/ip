# MiMi ☕

> “Productivity is never an accident. It is always the result of a commitment to excellence, intelligent planning, and focused effort.” – Paul J. Meyer

MiMi is a friendly personal task manager chatbot built in Java.  
It helps you organize tasks quickly without distraction — now with **cleaner UI, better commands, and a slick JavaFX GUI**!

- 📝 text-based + GUI modes
- ⚡ fast and responsive
- 💾 automatic saving and loading
- ⏳ supports due dates and periods
- 😺 cute visuals to make tasking fun

---

## 🚀 How to Use

All you need to do is:

1. download [MiMi here!](https://github.com/Huffle-Buffle/ip/releases/tag/A-Release)
2. double-click the `.jar`
3. start adding tasks
4. let MiMi handle the rest 😸

And yes, it’s still **FREE**!

---

## 🛠 Improvements Made

- Added `within` command for tasks within a date range
- Persistent task storage in `data/MiMi.txt`
- Integrated **JUnit tests** for Parser and TaskList
- Refactored into OOP classes: `MiMi`, `UiMasterList`, `Parser`, `Storage`, `TaskList`, `Todo`, `Deadline`, `Event`
- Added **JavaDoc comments** to public classes and methods
- Implemented **JavaFX GUI** with `Main`, `MainWindow`, and `DialogBox`
- Gave MiMi a personality

---

## ✨ Features

- Manage `todo`, `deadline`, `event`, and `within` tasks
- Search tasks with keywords (`find`)
- Mark tasks as done or undone
- Delete tasks easily
- Save/load tasks automatically from a file
- Friendly GUI interface with chat bubbles and images 🐱☕

---

## 💻 For Developers

If you're a Java programmer, MiMi is a great practice project.  
Here’s the entry point:

```java
public class Main {
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
```
## AI Usage Declaration! 
### Date: 29 August 2025
I(Megane Wong) used ChatGPT 5 to mostly assist with generating the Javadoc comments, commit messages from Level-7 up to A-Gradle were generated but now are all self written to meet the Git Standard.
I also used ChatGPT to help me generate the tests in JUnit Tests as I wanted the AI to get creative with the possible inputs as if I were to test it I would put inputs that I am sure would pass hence I used the AI to ensure it catches possible failed cases.
Lastly, I used it to assist me in correcting my mistakes as I was having some issues figuring out how to structure the Find feature at Level 9. 

### Date: 11 September 2025
As of today I have used GPT to help me check for where to improve the code quality as per this week's task as the topics for this week were so much that I had no idea where to even begin for the code 
quality hunt. Based on what it has suggested, I edited most of my magic strings and tried my best for the other areas, might have maybe missed a few. Other than that nothing really much else.

### Date: 17 September 2025
As per the previous weeks, mostly used GPT 5 to make the Javadoc comments for me. It also generated 2 more test cases for me in each of the unit tests. 
Also, it gave me some quality suggestions but that's pretty much it.