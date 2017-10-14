package com.yg.webshow.crawl.seeds.sample.clien;

public class ClienCommentData {
	private String comment ;
	private String oriComment ;
	private Member member ;
	private long boardSn ;
	private long commentSn ;
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getOriComment() {
		return oriComment;
	}
	public void setOriComment(String oriComment) {
		this.oriComment = oriComment;
	}
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	public long getBoardSn() {
		return boardSn;
	}
	public void setBoardSn(long boardSn) {
		this.boardSn = boardSn;
	}
	public long getCommentSn() {
		return commentSn;
	}
	public void setCommentSn(long commentSn) {
		this.commentSn = commentSn;
	}
}
