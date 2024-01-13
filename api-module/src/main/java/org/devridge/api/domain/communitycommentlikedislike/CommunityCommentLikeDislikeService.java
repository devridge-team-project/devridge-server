package org.devridge.api.domain.communitycommentlikedislike;

import org.devridge.api.domain.communitycomment.CommunityComment;
import org.devridge.api.githubsociallogintemp.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityCommentLikeDislikeService {

    private CommunityCommentLikeDislikeRepository communityCommentLikeDislikeRepository;

    @Autowired
    public CommunityCommentLikeDislikeService(
        CommunityCommentLikeDislikeRepository communityCommentLikeDislikeRepository) {
        this.communityCommentLikeDislikeRepository = communityCommentLikeDislikeRepository;
    }

    public void createLikeDisLike(Long memberId, Long commentId, Character status) {
        Member member = Member.builder().id(memberId).build();
        CommunityComment communityComment = CommunityComment.builder().id(commentId).build();
        CommunityCommentLikeDislike communityCommentLikeDislike = CommunityCommentLikeDislike.builder()
            .id(new CommunityCommentLikeDislikeId(memberId, commentId))
            .status(status)
            .member(member)
            .communityComment(communityComment)
            .build();
        communityCommentLikeDislikeRepository.save(communityCommentLikeDislike);
    }
}
