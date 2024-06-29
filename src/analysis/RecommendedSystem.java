package analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import grade.Grade;
import mainPage.ExamSet;

public class RecommendedSystem {
    private Grade userGrade;
    private List<ExamSet> examSetList;
    private List<int[]> topicList;
    private List<List<Integer>> examSetData;

    public RecommendedSystem(Grade userGrade, List<ExamSet> examSetList) {
        this.userGrade = userGrade;
        this.examSetList = examSetList;
        topicList = new ArrayList<>();
        examSetData = new ArrayList<>();

        int[] topics = {
                userGrade.getTopic1Grade(),
                userGrade.getTopic2Grade(),
                userGrade.getTopic3Grade(),
                userGrade.getTopic4Grade(),
                userGrade.getTopic5Grade(),
                userGrade.getTopic6Grade()
        };

        for (int i = 0; i < topics.length; i++) {
            int[] topicInfo = { i + 1, topics[i] }; 
            topicList.add(topicInfo);
        }

        for (ExamSet examSet : examSetList) {
            List<Integer> setInfo = new ArrayList<>();
            setInfo.add(examSet.getIdexam_set());
            setInfo.add(Integer.valueOf(examSet.getExam_diff()));
            setInfo.add(Integer.valueOf(examSet.getExam_context()));
            examSetData.add(setInfo);
        }
        quickSort(topicList, 0, topicList.size() - 1);
        reorderTopics();
    }

    private void quickSort(List<int[]> list, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(list, low, high);
            quickSort(list, low, pivotIndex - 1);
            quickSort(list, pivotIndex + 1, high);
        }
    }

    private int partition(List<int[]> list, int low, int high) {
        int pivot = list.get(high)[1];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (list.get(j)[1] < pivot) {
                i++;
                swap(list, i, j);
            }
        }

        swap(list, i + 1, high);
        return i + 1;
    }

    private void swap(List<int[]> list, int i, int j) {
        int[] temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    private void reorderTopics() {
        int[][] reorderedTopics = new int[topicList.size()][2];

        for (int i = 0; i < topicList.size(); i++) {
            reorderedTopics[i] = topicList.get(i);
        }

        Arrays.sort(reorderedTopics, Comparator.comparingInt(a -> a[1]));

        int[] desiredOrder = { 4, 2, 0, 1, 3, 5 };
        topicList.clear();

        for (int index : desiredOrder) {
            topicList.add(reorderedTopics[index]);
        }
    }

    public static void main(String[] args) {
        // Create sample Grade and ExamSetList
        Grade grade = new Grade(-1, 50, 10, 60, 20, 30, 70);
        List<ExamSet> examSetList = new ArrayList<>();
        examSetList.add(new ExamSet(-1, "test", "test", "3", "2"));
        examSetList.add(new ExamSet(-2, "test", "test", "4", "1"));
        examSetList.add(new ExamSet(-3, "test", "test", "2", "4"));

        // Initialize the recommended system
        RecommendedSystem system = new RecommendedSystem(grade, examSetList);

        // Now the topicList is sorted based on grade in ascending order
        for (int[] topicInfo : system.topicList) {
            System.out.println("Topic: " + topicInfo[0] + ", Grade: " + topicInfo[1]);
        }
    }
}