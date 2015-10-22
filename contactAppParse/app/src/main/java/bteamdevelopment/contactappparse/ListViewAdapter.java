package bteamdevelopment.contactappparse;

/**
 * Created by wkohusjr on 10/1/2015
 */
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    private List<Contact> contactList = null;
    private ArrayList<Contact> arraylist;

    public ListViewAdapter(Context context,
                           List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(contactList);
    }

    public class ViewHolder {
        TextView fName;
        TextView lName;
        TextView mobile;
        TextView email;
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.contact_list, null);
            // Locate the TextViews in listview_item.xml
            holder.fName = (TextView) view.findViewById(R.id.fName);
            holder.lName = (TextView) view.findViewById(R.id.lName);
            holder.mobile = (TextView) view.findViewById(R.id.mobile);
            holder.email = (TextView) view.findViewById(R.id.email);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.fName.setText(contactList.get(position).getfName());
        holder.lName.setText(contactList.get(position).getlName());
        holder.mobile.setText(contactList.get(position).getMobile());
        holder.email.setText(contactList.get(position).getEmail());

        // Listen for ListView Item Click
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Send single item click data to UpdateActivity Class
                Intent intent = new Intent(context, UpdateActivity.class);
                // Pass all data rank
                intent.putExtra("objectId",
                        contactList.get(position).getId());
                intent.putExtra("fName",
                        (contactList.get(position).getfName()));
                // Pass all data country
                intent.putExtra("lName",
                        (contactList.get(position).getlName()));
                // Pass all data population
                intent.putExtra("mobile",
                        (contactList.get(position).getMobile()));
                // Pass all data population
                intent.putExtra("email",
                        (contactList.get(position).getEmail()));
                intent.putExtra("home",
                        (contactList.get(position).getHome()));
                intent.putExtra("address",
                        (contactList.get(position).getAddress()));
                intent.putExtra("city",
                        (contactList.get(position).getCity()));
                intent.putExtra("state",
                        (contactList.get(position).getState()));
                intent.putExtra("zip",
                        (contactList.get(position).getZip()));
                // Start UpdateActivity Class
                context.startActivity(intent);
            }
        });
        return view;
    }

}