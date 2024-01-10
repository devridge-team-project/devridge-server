package org.devridge.api.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkillConstants {

    public static final Set<String> SKILL_SET;

    static {
        Set<String> tempSet = new HashSet<>();
        tempSet.add("mysql");
        tempSet.add("java");
        tempSet.add("c++");
        tempSet.add("python");
        tempSet.add("aws");
        tempSet.add("spring");
        tempSet.add("springboot");
        tempSet.add("javascript");
        tempSet.add("react");
        tempSet.add("nodejs");

        /**
         * 유저의 스킬셋 추가
         * */

        SKILL_SET = Collections.unmodifiableSet(tempSet);
    }

}
