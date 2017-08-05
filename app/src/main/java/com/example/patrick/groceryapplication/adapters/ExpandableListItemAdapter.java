package com.example.patrick.groceryapplication.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.example.patrick.groceryapplication.R;
import com.example.patrick.groceryapplication.models.FeaturedItem;
import com.example.patrick.groceryapplication.models.Item;

import java.util.ArrayList;

public class ExpandableListItemAdapter implements ExpandableListAdapter {

    private ArrayList<FeaturedItem> groupedList;
    private Context context;

    public ExpandableListItemAdapter(Context context, ArrayList<FeaturedItem> list){
        this.groupedList = list;
        this.context = context;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return groupedList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Item> childrenItems = groupedList.get(groupPosition).getGroceryList();
        return childrenItems.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupedList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Item child = groupedList.get(groupPosition).getGroceryList().get(childPosition);
        return child;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        FeaturedItem groupedItem = (FeaturedItem) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView heading = (TextView) convertView.findViewById(R.id.featured_list_title_view);
        heading.setText(groupedItem.getTitle().trim());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Item groceryItem = (Item) getGroup(childPosition);
        //Check to see of the view has been inflated
        if(convertView == null){//If not, we inflate the view here
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_list_item, null);
        }
        TextView childItem = (TextView) convertView.findViewById(R.id.featured_list_subtitle_view);
        childItem.setText(groceryItem.getItemName().trim());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
}
