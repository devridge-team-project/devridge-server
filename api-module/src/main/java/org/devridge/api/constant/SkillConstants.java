package org.devridge.api.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkillConstants {

    // TODO: [매우 중요...!!!] 이 부분 DB에서 검증하는 것으로 바꿔주세요. 추후 확장성을 위해서입니다.
    // 추가적으로 constant 또한 비즈니스 로직을 가질 수 없습니다.
    // 우선은 문자열 split을 받는다는 기준으로 이미 해당 스킬이 있는 경우 패스해주시고 없으면 저장하는 식으로 해주세요.
    // 이 부분은 저라면 스킬셋 아이디를 넘겨주는 형태로 할 것 같은데, 프론트 분들과 상의하시길 바랍니다.
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
