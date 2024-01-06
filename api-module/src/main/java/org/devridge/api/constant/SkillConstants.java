package org.devridge.api.constant;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SkillConstants {

    public static final Set<String> SKILL_SET;

    static {
        Set<String> tempSet = new HashSet<>();
        tempSet.add("MySQL");
        tempSet.add("Java");
        tempSet.add("C++");
        tempSet.add("AWS");
        tempSet.add("SpringBoot");
        tempSet.add("JavaScript");
        tempSet.add("nodeJS");

        /**
         * 유저의 스킬셋 추가
         * */

        SKILL_SET = Collections.unmodifiableSet(tempSet);
    }

    private SkillConstants() {
        // 이 클래스는 인스턴스화되지 않아야 함
    }
}
