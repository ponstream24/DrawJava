/**
 * Yuki Tetsuka
 *
 * Project: DrawJava
 * Description: A simple drawing application in Java.
 *
 * Copyright (c) 2023 Yuki Tetsuka. All rights reserved.
 * See the project repository at: https://github.com/ponstream24/DrawJava
 */

package enshuReport2_2023.util;

import enshuReport2_2023.Figure;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class HistoryCtrl {

    private Stack<ArrayList<LinkedList<Figure>>> undoList = new Stack<>();
    private Stack<ArrayList<LinkedList<Figure>>> redoList = new Stack<>();

    /**
     * 一つ戻る
     * @return null or Stack
     */
    public ArrayList<LinkedList<Figure>> undo(){

//        undoするものなし
        if( this.undoList.size() <= 0 ) return null;

//        ドゥー
        return doUndo();
    }

    private ArrayList<LinkedList<Figure>> doUndo(){

        ArrayList<LinkedList<Figure>> result = this.undoList.pop();

        this.redoList.push(result);

        return result;
    }

    /**
     * やり直し
     * @return null or Stack
     */
    public ArrayList<LinkedList<Figure>> redo(){

        if( this.redoList.size() <= 0 ) return null;

        return doRedo();
    }

    private ArrayList<LinkedList<Figure>> doRedo(){

        ArrayList<LinkedList<Figure>> result = this.redoList.pop();

        this.undoList.push(result);

        return result;
    }

    /**
     * 履歴に追加
     * @return 必要なし
     */
    public void add(ArrayList<LinkedList<Figure>> history){

        this.undoList.push(history);
        this.redoList.clear();
    }

    public Stack<ArrayList<LinkedList<Figure>>> getUndoList() {
        return this.undoList;
    }

    public void setUndoList(Stack<ArrayList<LinkedList<Figure>>> undoList) {
        this.undoList = undoList;
    }

    public Stack<ArrayList<LinkedList<Figure>>> getRedoList() {
        return this.redoList;
    }

    public void setRedoList(Stack<ArrayList<LinkedList<Figure>>> redoList) {
        this.redoList = redoList;
    }
}
