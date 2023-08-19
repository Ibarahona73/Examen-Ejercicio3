package hn.uth.examen201930010221.ui.inicio;

public interface OnItemClickListener <T>{

    void onItemClick(T data);

    void onItemClickDelete(T data);

    void onItemClickShared(T data);
}
