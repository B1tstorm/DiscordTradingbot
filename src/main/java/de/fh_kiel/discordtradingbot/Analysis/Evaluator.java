package de.fh_kiel.discordtradingbot.Analysis;

import de.fh_kiel.discordtradingbot.Config;
import de.fh_kiel.discordtradingbot.Holdings.Letter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Evaluator implements Subscriber {

    private volatile static Evaluator onlyInstance;
    private final List<LetterStatisticsItem> letterStatistics;

    Evaluator() {
        letterStatistics = new ArrayList<>();
        for(int i = 0 ; i < 26; i++) {
            letterStatistics.add(new LetterStatisticsItem(Config.indexToChar(i)));
        }

    }

    public List<LetterStatisticsItem> getLetterStatistics() {
        return letterStatistics;
    }

    public static Evaluator getInstance() {
        if (onlyInstance == null) {
            synchronized (Evaluator.class) {
                if (onlyInstance == null) {
                    onlyInstance = new Evaluator();
                }
            }
        }
        return onlyInstance;
    }

    @Override
    public void update(Letter l) {
        int index = Config.charToIndex(l.getLetter());
        this.letterStatistics.get(index).updateValues(l);
    }

    public Integer getCurrentLetterValue(Letter l) {
        int index = Config.charToIndex(l.getLetter());
        return letterStatistics.get(index).maxValue;
    }

    // getCurrentAvgValue MaxValue ...

    public class LetterStatisticsItem {
        private char letter;
        private final List<Integer> tradedLetterValues = new ArrayList<>();
        private Integer amountTraded = 0;
        private Double averageValue = 0d;
        private Integer minValue = 0;
        private Integer maxValue = 0;
        private Double increaseInValue = 0d;

        LetterStatisticsItem(char c) {
            this.letter = c;
        }


        protected void updateValues(Letter l) {
            this.tradedLetterValues.add(l.getValue());
            this.amountTraded = this.tradedLetterValues.size();
            this.averageValue = getAverageValue(this.tradedLetterValues);
            this.maxValue = Collections.max(tradedLetterValues);
            this.minValue = Collections.min(tradedLetterValues);
            this.increaseInValue = getValueIncreasePercentage(l.getValue());
        }

        private Double getAverageValue(List<Integer> list) {
            Double result = 0d;
            for (Integer i : list) {
                result += i;
            }
            return result / (list.size());
        }

        private double getValueIncreasePercentage(Integer newValue) {
            return ((double) newValue) / tradedLetterValues.get(0);
        }

        public char getLetter() {
            return letter;
        }

        public List<Integer> getTradedLetterValues() {
            return tradedLetterValues;
        }

        public Integer getMaxValue() {
            return maxValue;
        }
    }

}