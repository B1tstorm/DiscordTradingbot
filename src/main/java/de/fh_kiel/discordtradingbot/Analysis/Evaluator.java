package de.fh_kiel.discordtradingbot.Analysis;

import de.fh_kiel.discordtradingbot.Config;
import de.fh_kiel.discordtradingbot.Holdings.Letter;
import de.fh_kiel.discordtradingbot.Interaction.EventType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Evaluator implements LetterListener, LetterPublisher {

    private volatile static Evaluator onlyInstance;
    private final List<LetterStatisticsItem> letterStatistics;



    Evaluator() {
        letterStatistics = new ArrayList<>();
        for(int i = 0 ; i < 26; i++) {
            letterStatistics.add(
                    new LetterStatisticsItem((Config.staticLetterValues[i] * 5) + 5, Config.indexToChar(i)));
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
    public void update(Letter l, EventType source) {
        if (source != EventType.HISTORY) return;

        int i = Config.charToIndex(l.getLetter());
        this.letterStatistics.get(i).updateValues(l);

        notifySubscribers(this.letterStatistics.get(i).letter);
    }

    public void registerSubscriber(LetterListener s) {
        this.subscribers.add(s);
    }

    @Override
    public void removeSubscriber(LetterListener s) {
        if (this.subscribers.contains(s)) {
            this.subscribers.remove(s);
        } else {
            System.out.println("The Subscriber has not subscribed and cannot be removed.");
        }
    }

    @Override
    public void notifySubscribers(Letter l) {
        for (LetterListener s : subscribers) {
            s.update(l, EventType.EVALUATION);
        }
    }

    public Integer getCurrentLetterValue(Letter l) {
        int index = Config.charToIndex(l.getLetter());
        return letterStatistics.get(index).maxValue;
    }

    // getCurrentAvgValue MaxValue ...

    public class LetterStatisticsItem {
        private final Letter letter;
        private final List<Integer> tradedLetterValues = new ArrayList<>();
        private Integer amountTraded = 0;
        private double averageValue = 0d;
        private double staticValue = 0d;
        private Integer minValue = 0;
        private Integer maxValue = 0;
        private Double increaseInValue = 0d;

        public LetterStatisticsItem(double staticValue, char c) {
            this.staticValue = staticValue;
            this.averageValue = staticValue;
            this.maxValue = (int) staticValue;
            this.letter = new Letter(c, -1, (int) this.averageValue);
        }

        protected void updateValues(Letter l) {
            this.tradedLetterValues.add(l.getValue());
            this.amountTraded = this.tradedLetterValues.size();
            this.maxValue = Math.max(Collections.max(tradedLetterValues), this.getMaxValue());
            this.minValue = Collections.min(tradedLetterValues);

            this.averageValue = ((getAverageValue(this.tradedLetterValues) * (1.0-(1.0/this.amountTraded))) + (this.staticValue * (1.0/this.amountTraded)));
            //this.increaseInValue = getValueIncreasePercentage(l.getValue());
            this.letter.setValue((int) this.averageValue);
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

        public Letter getLetter() {
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