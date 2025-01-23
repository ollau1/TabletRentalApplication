package com.example.tabletrentalapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tabletrentalapplication.database.DatabaseHelper;
import com.example.tabletrentalapplication.models.RentalRequest;

import java.util.List;
import java.util.stream.Collectors;

public class AdminActivity extends AppCompatActivity {

    private Spinner tabletBrandSpinner, cableTypeSpinner;
    private EditText dateFilter;
    private Button filterButton;
    private ListView rentalRequestsListView;

    private DatabaseHelper databaseHelper;
    private List<RentalRequest> allRentalRequests;
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize UI components
        tabletBrandSpinner = findViewById(R.id.tabletBrandSpinner);
        cableTypeSpinner = findViewById(R.id.cableTypeSpinner);
        dateFilter = findViewById(R.id.dateFilter);
        filterButton = findViewById(R.id.filterButton);
        rentalRequestsListView = findViewById(R.id.rentalRequestsListView);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Fetch all rental requests from the database
        allRentalRequests = databaseHelper.getAllRentalRequests();

        // Set up spinners and display the rental requests
        setupSpinners();
        displayRentalRequests(allRentalRequests);

        // Handle filter button click
        filterButton.setOnClickListener(v -> applyFilters());

        // Handle item clicks to show confirmation dialog
        rentalRequestsListView.setOnItemClickListener((parent, view, position, id) -> {
            RentalRequest selectedRequest = allRentalRequests.get(position);
            showConfirmationDialog(selectedRequest);
        });
    }

    // Populate the spinners with options
    private void setupSpinners() {
        // Set up Tablet Brand Spinner
        ArrayAdapter<CharSequence> tabletAdapter = ArrayAdapter.createFromResource(this,
                R.array.tablet_brands, android.R.layout.simple_spinner_item);
        tabletAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tabletBrandSpinner.setAdapter(tabletAdapter);

        // Set up Cable Type Spinner
        ArrayAdapter<CharSequence> cableAdapter = ArrayAdapter.createFromResource(this,
                R.array.cable_types, android.R.layout.simple_spinner_item);
        cableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cableTypeSpinner.setAdapter(cableAdapter);
    }

    // Display the rental requests in the ListView
    private void displayRentalRequests(List<RentalRequest> rentalRequests) {
        // Convert RentalRequest objects to strings for display
        List<String> requestStrings = rentalRequests.stream()
                .map(req -> String.format("%s | %s | %s | %s",
                        req.getTabletBrand(),
                        req.isIncludeCable() ? req.getCableType() : "No Cable",
                        req.getLenderName(),
                        req.getSubmissionDate()))
                .collect(Collectors.toList());

        // Set up the ListView adapter
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requestStrings);
        rentalRequestsListView.setAdapter(listAdapter);
    }

    // Filter the rental requests based on user input
    private void applyFilters() {
        // Get selected filter values
        String selectedBrand = tabletBrandSpinner.getSelectedItem().toString();
        String selectedCableType = cableTypeSpinner.getSelectedItem().toString();
        String date = dateFilter.getText().toString().trim();

        // Filter the list
        List<RentalRequest> filteredList = allRentalRequests.stream()
                .filter(req -> (selectedBrand.equals("All") || req.getTabletBrand().equals(selectedBrand)))
                .filter(req -> (selectedCableType.equals("All") || (req.isIncludeCable() && req.getCableType().equals(selectedCableType))))
                .filter(req -> (date.isEmpty() || req.getSubmissionDate().startsWith(date)))
                .collect(Collectors.toList());

        // Debugging: Log the filter values
        Log.d("AdminActivity", "Selected Brand: " + selectedBrand);
        Log.d("AdminActivity", "Selected Cable Type: " + selectedCableType);
        Log.d("AdminActivity", "Entered Date: " + date);
        Log.d("AdminActivity", "Filtered List Size: " + filteredList.size());

        // Refresh the ListView with the filtered list
        displayRentalRequests(filteredList);
    }

    // Show a confirmation dialog to mark the rental request as returned
    private void showConfirmationDialog(RentalRequest rentalRequest) {
        new AlertDialog.Builder(this)
                .setTitle("Mark as Returned")
                .setMessage("Are you sure you want to mark this as returned?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Delete the rental request from the database
                    Log.d("AdminActivity", "Attempting to delete RentalRequest with ID: " + rentalRequest.getId());
                    databaseHelper.deleteRentalRequest(rentalRequest);

                    // Update the list and refresh the ListView
                    allRentalRequests = databaseHelper.getAllRentalRequests();
                    displayRentalRequests(allRentalRequests);

                    Toast.makeText(this, "Rental Request marked as returned.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null) // Do nothing on "No"
                .show();
    }
}