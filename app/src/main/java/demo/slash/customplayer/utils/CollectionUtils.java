package demo.slash.customplayer.utils;

import java.util.Iterator;
import java.util.List;

import demo.slash.customplayer.bean.VideoItem;

/**
 * Why did I wrote this ?
 * Created by PICO-USER on 2016/12/16.
 */
public class CollectionUtils {

    public static void addToList(List<VideoItem> list,VideoItem item){
        if(list==null){
            return;
        }
        list.add(item);
    }

    public static void addToList(List<VideoItem> list,VideoItem item,int position){
        if(list==null){
            return ;
        }
        if(position>list.size()){
            position = list.size();
        }
        if(position<0){
            return ;
        }
        list.add(position,item);
    }

    public static void addToList(List<VideoItem> list,String path){
        if(list ==null){
            return;
        }
        VideoItem videoItem = CommonUtils.fromPath2Bean(path);
        if(videoItem!=null){
            list.add(videoItem);
        }
    }

    public static void removeFromList(List<VideoItem> list,VideoItem item){
        if(list==null){
            return;
        }
        removeFromList(list,item.getPath());
    }

    public static void removeFromList(List<VideoItem> list,String path){
        if(list==null){
            return ;
        }
        Iterator<VideoItem> iterator = list.iterator();
        while (iterator.hasNext()){
            VideoItem next = iterator.next();
            if(next.getPath().equals(path)){
                list.remove(next);
            }
            return ;
        }
    }

}
