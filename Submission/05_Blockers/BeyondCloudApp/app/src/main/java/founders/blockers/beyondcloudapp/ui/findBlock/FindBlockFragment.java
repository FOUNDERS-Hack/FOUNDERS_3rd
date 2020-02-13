package founders.blockers.beyondcloudapp.ui.findBlock;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import founders.blockers.beyondcloudapp.MyLoveFormActivity;
import founders.blockers.beyondcloudapp.R;
import founders.blockers.beyondcloudapp.SearchActivity;

public class FindBlockFragment extends Fragment {

    Button searchBtn;

    private FindBlockViewModel findBlockViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        findBlockViewModel =
                ViewModelProviders.of(this).get(FindBlockViewModel.class);
        View root = inflater.inflate(R.layout.fragment_findblock, container, false);

        searchBtn = (Button) root.findViewById(R.id.add_btn_1);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}