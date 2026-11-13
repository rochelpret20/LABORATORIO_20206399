package com.stfgames.puzzle.model;

public class PuzzleState{
    public String getBoard() {
        return board;
    }

    public int getSize() {
        return size;
    }

    public String getImage() {
        return image;
    }

    public boolean isPlaying() {
        return playing;
    }

    private String board;
    private String image;
    private int size;
    private boolean playing;
    public PuzzleState(String board, String image, int size, boolean playing){
        this.board=board;
        this.image=image;
        this.size=size;
        this.playing=playing;
    }
}


