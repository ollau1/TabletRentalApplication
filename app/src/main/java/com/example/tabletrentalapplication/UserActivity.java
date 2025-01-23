package com.example.tabletrentalapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tabletrentalapplication.database.DatabaseHelper;
import com.example.tabletrentalapplication.models.RentalRequest;

public class UserActivity extends AppCompatActivity {

    private RadioGroup tabletGroup, cableGroup;
    private CheckBox includeCable;
    private EditText lenderName, contactNumber;
    private Button rentButton;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Initialize UI components
        tabletGroup = findViewById(R.id.tabletGroup);
        includeCable = findViewById(R.id.includeCable);
        cableGroup = findViewById(R.id.cableGroup);
        lenderName = findViewById(R.id.lenderName);
        contactNumber = findViewById(R.id.contactNumber);
        rentButton = findViewById(R.id.rentButton);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Toggle cable options visibility
        includeCable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cableGroup.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        // Handle Rent button click
        rentButton.setOnClickListener(v -> {
            // Get selected tablet brand
            int selectedTabletId = tabletGroup.getCheckedRadioButtonId();

            // Check if a tablet brand is selected
            if (selectedTabletId == -1) {
                Toast.makeText(this, "Please select a tablet brand.", Toast.LENGTH_SHORT).show();
                return;
            }

            String tabletBrand = selectedTabletId == R.id.brandA ? "BrandA" : "BrandB";

            // Get cable inclusion details
            boolean isCableIncluded = includeCable.isChecked();
            String cableType = null;
            if (isCableIncluded) {
                int selectedCableId = cableGroup.getCheckedRadioButtonId();
                if (selectedCableId == R.id.usbC) cableType = "USB-C";
                else if (selectedCableId == R.id.microUSB) cableType = "Micro-USB";
            }

            // Get lender information
            String name = lenderName.getText().toString().trim();
            String contact = contactNumber.getText().toString().trim();

            // Validate inputs
            if (name.isEmpty() || contact.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create RentalRequest
            RentalRequest rentalRequest = new RentalRequest(tabletBrand, isCableIncluded, cableType, name, contact);

            // Save RentalRequest to the database
            long id = databaseHelper.insertRentalRequest(rentalRequest);
            if (id > 0) {
                // Show overview popup
                showOverviewPopup(rentalRequest);

                // Clear all input fields
                clearInputFields();
            } else {
                Toast.makeText(this, "Failed to submit request", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showOverviewPopup(RentalRequest rentalRequest) {
        // Prepare the message
        String overviewMessage = String.format(
                "Tablet Brand: %s\nCable Included: %s\nCable Type: %s\nName: %s\nContact: %s",
                rentalRequest.getTabletBrand(),
                rentalRequest.isIncludeCable() ? "Yes" : "No",
                rentalRequest.isIncludeCable() ? rentalRequest.getCableType() : "N/A",
                rentalRequest.getLenderName(),
                rentalRequest.getContactNumber()
        );

        // Show a popup dialog
        new AlertDialog.Builder(this)
                .setTitle("Rental Overview")
                .setMessage(overviewMessage)
                .setPositiveButton("OK", null)
                .show();
    }

    private void clearInputFields() {
        // Clear tablet selection
        tabletGroup.clearCheck();

        // Clear cable options
        includeCable.setChecked(false);
        cableGroup.clearCheck();
        cableGroup.setVisibility(View.GONE);

        // Clear text fields
        lenderName.setText("");
        contactNumber.setText("");
    }
}