package founders.blockers.beyondcloudapp.ui.addBlock;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import founders.blockers.beyondcloudapp.MeFormActivity;
import founders.blockers.beyondcloudapp.R;
import founders.blockers.beyondcloudapp.MyLoveFormActivity;

public class AddBlockFragment extends Fragment {

    Button myLoveBtn;
    Button meBtn;

    private AddBlockViewModel addBlockViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addBlockViewModel =
                ViewModelProviders.of(this).get(AddBlockViewModel.class);
        View root = inflater.inflate(R.layout.fragment_addblock, container, false);

        myLoveBtn = (Button) root.findViewById(R.id.add_btn_1);
        meBtn = (Button) root.findViewById(R.id.add_btn_2);


        myLoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyLoveFormActivity.class);
                startActivity(intent);
                return;
            }
        });

        meBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MeFormActivity.class);
                startActivity(intent);
            }
        });


        return root;
    }





}