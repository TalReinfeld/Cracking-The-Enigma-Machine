package logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Permutations implements Iterable<List<Integer>>{

    private List<List<Integer>> permutations = new ArrayList<>();

    public Permutations(int[] nums) {

        Permutation(0, nums);
    }

    private void Permutation(int i, int[] nums) {

         if (i == nums.length - 1) {
            List<Integer> list = new ArrayList<>();
            for (int n : nums){
                list.add(n);
            }

            permutations.add(list);
        } else {
            for (int j = i, l = nums.length; j < l; j++) {
                int temp = nums[j];
                nums[j] = nums[i];
                nums[i] = temp;
                Permutation(i + 1, nums);
                temp = nums[j];
                nums[j] = nums[i];
                nums[i] = temp;
            }
        }
    }

    @Override
    public Iterator<List<Integer>> iterator() {

        return new  Iterator<List<Integer>>() {

           private int index = 0;

           @Override
           public boolean hasNext() {
               return permutations.size() > index;
           }

           @Override
           public List<Integer> next() {
               return permutations.get(index++);
           }
       };
    }

    private Iterator<List<Integer>> itr = iterator();

    public boolean hasNext(){
        return itr.hasNext();
    }

    public List<Integer> next(){
        return itr.next();
    }
}