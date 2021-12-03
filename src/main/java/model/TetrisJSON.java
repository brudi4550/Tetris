package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class TetrisJSON {
    private JSONObject json;
    private final JSONParser parser = new JSONParser();
    private final int MAX_ENTRIES = 100;

    private void refreshJSON() {
        try (Reader reader = new FileReader("tetrisdata.json")) {
            json = (JSONObject) parser.parse(reader);
        } catch (Exception e) {
            System.out.println("JSON file could not be refreshed. Trying to reload whole file.");
            loadJSON();
        }
    }

    private void loadJSON() {
        File f = new File("tetrisdata.json");
        if (f.exists() && !f.isDirectory()) {
            try (Reader reader = new FileReader("tetrisdata.json")) {
                json = (JSONObject) parser.parse(reader);
                trimAll();
            } catch (Exception e) {
                System.out.println("tetrisdata file couldn't be read, a new empty file will be created");
                json = new JSONObject();
                initJSON();
                writeJSON();
            }
        } else {
            try {
                f.createNewFile();
                json = new JSONObject();
                initJSON();
                writeJSON();
            } catch (IOException e) {
                System.out.println("tetrisdata file couldn't be created.");
                e.printStackTrace();
            }
        }
    }

    public void trimAll() {
        trim("easyHighscores", size("easyHighscores") - MAX_ENTRIES);
        trim("normalHighscores", size("normalHighscores") - MAX_ENTRIES);
        trim("hardHighscores", size("hardHighscores") - MAX_ENTRIES);
        trim("insaneHighscores", size("insaneHighscores") - MAX_ENTRIES);
    }

    public void trim(String key, int amount) {
        for (int i = 0; i < amount; i++) {
            removeLowestHighscore(key);
        }
    }

    public boolean isHighscore(String category, int score) {
        return getList(category).stream().anyMatch(s -> s.getScore() < score) || size(category) <= 100;
    }

    public void addHighscore(String category, String name, int score) {
        refreshJSON();
        if (size(category) + 1 > MAX_ENTRIES) trim(category,json.size() + 1 - MAX_ENTRIES);
        JSONObject highscoreObj = new JSONObject();
        highscoreObj.put("name", name);
        highscoreObj.put("score", score);
        LocalTime time = LocalTime.now();
        int hour = time.getHour();
        int minute = time.getMinute();
        int second = time.getSecond();
        LocalDate date = LocalDate.now();
        int day = date.getDayOfMonth();
        int month = date.getMonthValue();
        int year = date.getYear();
        highscoreObj.put("date", day+"."+month+"."+year+" at "+hour+":"+
                (minute < 10 ? "0"+minute : minute)+":"+
                (second < 10 ? "0"+second : second));
        JSONArray highscores = (JSONArray) json.get(category);
        highscores.add(highscoreObj);
        writeJSON();
    }

    private void removeLowestHighscore(String key) {
        refreshJSON();
        if (json.isEmpty()) return;
        JSONArray array = (JSONArray) json.get(key);
        JSONObject lowestScoreObj = null;
        int lowestScore = Integer.MAX_VALUE;
        for (Object o : array) {
            JSONObject current = (JSONObject) o;
            //fix type casting, this looks like shit
            int currentScore = ((Long)current.get("score")).intValue();
            if (currentScore < lowestScore) {
                lowestScore = currentScore;
                lowestScoreObj = current;
            }
        }
        if (lowestScoreObj != null) array.remove(lowestScoreObj);
        writeJSON();
    }

    public List<Score> getList(String key) {
        refreshJSON();
        if (json.isEmpty() || json.get(key) == null) return null;
        List<Score> list = new ArrayList<>();
        JSONArray highscores = (JSONArray) json.get(key);
        for (Object o : highscores) {
            JSONObject next = (JSONObject) o;
            String name = (String) next.get("name");
            //same shit here
            Long longScore = (Long) next.get("score");
            int score = longScore.intValue();
            String date = (String) next.get("date");
            Score highscore = new Score(name, score, date);
            list.add(highscore);
        }
        list.sort(Comparator.comparingInt(Score::getScore));
        Collections.reverse(list);
        return list;
    }

    public void clear() {
        json.clear();
        initJSON();
        writeJSON();
    }

    private void initJSON() {
        JSONArray easyHighscores = new JSONArray();
        JSONArray normalHighscores = new JSONArray();
        JSONArray hardHighscores = new JSONArray();
        JSONArray insaneHighscores = new JSONArray();
        json.put("easyHighscores", easyHighscores);
        json.put("normalHighscores", normalHighscores);
        json.put("hardHighscores", hardHighscores);
        json.put("insaneHighscores", insaneHighscores);
    }

    private int size(String key) {
        refreshJSON();
        if (json.isEmpty()) return 0;
        JSONArray array = (JSONArray) json.get(key);
        return array.size();
    }

    private void writeJSON() {
        try (FileWriter file = new FileWriter("tetrisdata.json")){
            file.write(json.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
