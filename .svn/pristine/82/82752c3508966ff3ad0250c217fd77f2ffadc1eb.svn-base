package com.smarcom.smarpay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.smarcom.smarpay.R;
import com.smarcom.smarpay.cloudapi.model.DeviceItem;
import com.smarcom.smarpay.helper.DeviceListAdapterListener;
import com.smarcom.smarpay.helper.FontHelper;

import java.util.ArrayList;
import java.util.List;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceListViewHolder> implements Filterable {

    private LayoutInflater layoutInflater;
    private List<DeviceItemWithDistance> originalData;
    private List<DeviceItemWithDistance> data = new ArrayList<>();
    private DeviceListAdapterListener deviceListAdapterListener;
    private MachineFilter machineFilter;

    public enum MachineFilter {
        Address,
        DeviceId
    }

    public DeviceListAdapter(Context context, DeviceListAdapterListener deviceListAdapterListener) {
        layoutInflater = LayoutInflater.from(context);
        this.deviceListAdapterListener = deviceListAdapterListener;
        this.machineFilter = MachineFilter.Address;
    }

    public DeviceListAdapter(Context context, DeviceListAdapterListener deviceListAdapterListener, MachineFilter machineFilter) {
        layoutInflater = LayoutInflater.from(context);
        this.deviceListAdapterListener = deviceListAdapterListener;
        this.machineFilter = machineFilter;
    }

    public List<DeviceItemWithDistance> getData() {
        return data;
    }

//    public List<String> getDistances() {
//        return distances;
//    }

    @Override
    public DeviceListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.devcie_list_item, viewGroup, false);
        return new DeviceListViewHolder(view, deviceListAdapterListener);
    }

    @Override
    public void onBindViewHolder(DeviceListViewHolder viewHolder, int i) {
//        DeviceItem current = data.get(i);
        DeviceItemWithDistance current = data.get(i);
        viewHolder.textView.setText(current.getDeviceItem().getStreet());
        viewHolder.tv.setText((current.getDeviceItem().getCity()));
        String distance = current.getDistance();
        if (distance != null) {
            viewHolder.distance.setText(distance);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (originalData == null) {
                    originalData = new ArrayList<>(getData());
                }

                if (TextUtils.isEmpty(constraint)) {
                    results.values = originalData;
                    results.count = originalData.size();
                } else {

                    String query = constraint.toString();
                    List<DeviceItemWithDistance> deviceItems = new ArrayList<>();
                    for (DeviceItemWithDistance item: originalData) {
                        if (machineFilter == MachineFilter.Address) {
                            if (item.getDeviceItem().getCity().toUpperCase().contains(query.toUpperCase()) || item.getDeviceItem().getStreet().toUpperCase().contains(query.toUpperCase())) {
                                deviceItems.add(item);
                            }
                        } else {
                            if (item.getDeviceItem().getDeviceId().toUpperCase().contains(query.toUpperCase())) {
                                deviceItems.add(item);
                            }
                        }

                        results.values = deviceItems;
                        results.count = deviceItems.size();

                    }
                }

                return results;
            }

                @Override
                protected void publishResults (CharSequence constraint, FilterResults results) {
                    getData().clear();

                    if (results != null && results.values != null) {
                        getData().addAll((List<DeviceItemWithDistance>) results.values);
                    }

                    notifyDataSetChanged();
                }
        };
    }

    public class DeviceListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        private TextView textView;
        private  TextView tv;
        private TextView distance;
        private DeviceListAdapterListener deviceListAdapterListener;

        public DeviceListViewHolder(View itemView, DeviceListAdapterListener deviceListAdapterListener) {
            super(itemView);

            this.deviceListAdapterListener = deviceListAdapterListener;

            textView = (TextView) itemView.findViewById(R.id.textview_search1);
            tv = (TextView) itemView.findViewById(R.id.textview_search2);
            distance = (TextView) itemView.findViewById(R.id.textview_search3);

            textView.setTypeface(FontHelper.getBoldFont(itemView.getContext()));
            tv.setTypeface(FontHelper.getBoldFont(itemView.getContext()));
            distance.setTypeface(FontHelper.getBoldFont(itemView.getContext()));

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            deviceListAdapterListener.onAdapterItemClick(getData().get(position).getDeviceItem().getDeviceId());
        }
    }

    public static class DeviceItemWithDistance {

        private DeviceItem deviceItem;
        private String distance;

        public DeviceItemWithDistance(DeviceItem deviceItem) {
            this.deviceItem = deviceItem;
            distance = null;
        }

        public DeviceItem getDeviceItem() {
            return deviceItem;
        }

        public void setDeviceItem(DeviceItem deviceItem) {
            this.deviceItem = deviceItem;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DeviceItemWithDistance)) return false;

            DeviceItemWithDistance that = (DeviceItemWithDistance) o;

            if (deviceItem != null ? !deviceItem.equals(that.deviceItem) : that.deviceItem != null)
                return false;
            return !(distance != null ? !distance.equals(that.distance) : that.distance != null);

        }

        @Override
        public int hashCode() {
            int result = deviceItem != null ? deviceItem.hashCode() : 0;
            result = 31 * result + (distance != null ? distance.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "DeviceItemWithDistance{" +
                    "deviceItem=" + deviceItem +
                    ", distance='" + distance + '\'' +
                    '}';
        }
    }
}