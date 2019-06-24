package com.github.tetrisanalyzer.settings;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemSettings {
    public SettingsReader reader;
    public Map<String, GameSettings> gameSettings = new HashMap<>();
    public Map<String, Map> pieceGeneratorSettings = new HashMap<>();
    public Map<String, Map> boardEvaluatorSettings = new HashMap<>();

    public static SystemSettings fromString(String settings) {
        try {
            return new SystemSettings((Map) new YamlReader(settings).read());
        } catch (YamlException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static SystemSettings fromFile(String filename) {
        try {
            //File file = new File(filename);
            Map content = (Map)new YamlReader(new FileReader(filename)).read();
            return new SystemSettings(content);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private SystemSettings(Map settings) {
        this.reader = new SettingsReader(settings, "system settings");
        setTetrisRules();
        setPieceGenerators();
        setBoardEvaluators();
    }

    private void setTetrisRules() {
        List<Map> tetrisRules = reader.readMaps("tetris rules");

        for (Map map : tetrisRules) {
            String id = new SettingsReader(map, "system settings").readString("id");
            gameSettings.put(id, CustomGameSettings.fromMap(map));
        }
    }

    private void setPieceGenerators() {
        List<Map> pieceGenerators = reader.readMaps("piece generators");

        for (Map settings : pieceGenerators) {
            SettingsReader mapReader = new SettingsReader(settings, "piece generator");
            String id = mapReader.readString("id");
            this.pieceGeneratorSettings.put(id, settings);
        }
    }

    private void setBoardEvaluators() {
        List<Map> boardEvaluators = reader.readMaps("board evaluators");

        for (Map settings : boardEvaluators) {
            SettingsReader mapReader = new SettingsReader(settings, "piece generator");
            String id = mapReader.readString("id");
            boardEvaluatorSettings.put(id, settings);
        }
    }

    public GameSettings findTetrisRules(String id) {
        if (!gameSettings.containsKey(id)) {
            throw new IllegalArgumentException("Could not find game rule id '" + id + "' in " + gameSettings);
        }
        return gameSettings.get(id);
    }

    public Map findBoardEvaluatorSettings(String id) {
        if (!boardEvaluatorSettings.containsKey(id)) {
            throw new IllegalArgumentException("Could not find board evaluator id '" + id + "' in " + gameSettings);
        }
        return boardEvaluatorSettings.get(id);
    }

    public Map findPieceGeneratorSettings(String id) {
        if (!pieceGeneratorSettings.containsKey(id)) {
            throw new IllegalArgumentException("Could not find board piece generator id '" + id + "' in " + gameSettings);
        }
        return pieceGeneratorSettings.get(id);
    }
}
