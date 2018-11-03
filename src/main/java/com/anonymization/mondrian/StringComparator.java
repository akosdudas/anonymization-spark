package com.anonymization.mondrian;

import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharSequenceNodeFactory;
import com.googlecode.concurrenttrees.solver.LCSubstringSolver;

import java.io.Serializable;
import java.util.Collection;

public class StringComparator implements Serializable {

    public String getLongestCommonSubstring(String string1, String string2) {
        LCSubstringSolver solver = new LCSubstringSolver(new DefaultCharSequenceNodeFactory());
        solver.add(string1);
        solver.add(string2);
        return solver.getLongestCommonSubstring().toString();
    }
    public String getLongestCommonSubstring(Collection<String> strings) {
        LCSubstringSolver solver = new LCSubstringSolver(new DefaultCharSequenceNodeFactory());
        for (String s : strings) {
            solver.add(s);
        }
        return solver.getLongestCommonSubstring().toString();
    }
}
