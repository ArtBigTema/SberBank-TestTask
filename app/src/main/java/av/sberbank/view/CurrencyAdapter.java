package av.sberbank.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import av.sberbank.model.Currency;

/**
 * Created by Artem on 12.04.2017.
 */

public class CurrencyAdapter extends ArrayAdapter<Currency> {

    private int currentCurrencyPosition;

    public CurrencyAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View view;
        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(android.R.layout.simple_list_item_single_choice, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        Currency currency = getItem(position);
        holder.bind(currency, currentCurrencyPosition == position);
        return view;
    }

    public Currency getSelectedItem(){
        return getItem(currentCurrencyPosition);
    }

    public void updateSelected(int which) {
        currentCurrencyPosition = which;
        notifyDataSetChanged();
    }

    public int getCurrentCurrencyPosition() {
        return currentCurrencyPosition;
    }

    public void setCurrentCurrencyPosition(int currentCurrencyPosition) {
        this.currentCurrencyPosition = currentCurrencyPosition;
    }

    private static class ViewHolder {
        private CheckedTextView title;

        ViewHolder(View rootView) {
            title = (CheckedTextView) rootView.findViewById(android.R.id.text1);
        }

        void bind(Currency currency, boolean checked) {
            title.setText(currency.getCharCodePlusName());
            title.setChecked(checked);
        }
    }
}