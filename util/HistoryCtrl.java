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

    private final Stack<ArrayList<LinkedList<Figure>>> undoList = new Stack<>();
    private final Stack<ArrayList<LinkedList<Figure>>> redoList = new Stack<>();

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

        for (ArrayList<LinkedList<Figure>> linkedList : undoList ) {
            System.out.println(linkedList);
        }

        System.out.println("↓");
        System.out.println(result);

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

        for (ArrayList<LinkedList<Figure>> linkedList : redoList ) {
            System.out.println(linkedList);
        }

        System.out.println("↓");
        System.out.println(result);

        return result;
    }

    /**
     * 履歴に追加
     */
    public void add(ArrayList<LinkedList<Figure>> history){

//        直前のリスト数と同じ
        if( this.undoList.size() > 1 && history.size() == this.undoList.size() ){
            System.out.println("同じ : " + history.size());
//            return;
        }

        ArrayList<LinkedList<Figure>> h = new ArrayList<>(history);

        this.undoList.push(h);
        this.redoList.clear();

        System.out.println();

        for (ArrayList<LinkedList<Figure>> linkedList : undoList ) {
            System.out.println(linkedList);
        }
    }

    public Stack<ArrayList<LinkedList<Figure>>> getUndoList() {

        System.out.println("参照 : " + this.undoList);
        return this.undoList;
    }

    public Stack<ArrayList<LinkedList<Figure>>> getRedoList() {
        return this.redoList;
    }

    public ArrayList<LinkedList<Figure>> getNextUndo(){

        if( this.undoList.size() == 0 ) return null;

        return this.undoList.get( this.undoList.size() - 1);
    }
    public ArrayList<LinkedList<Figure>> getNextRedo(){

        if( this.redoList.size() == 0 ) return null;

        return this.redoList.get( this.redoList.size() - 1);
    }
}
