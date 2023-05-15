package com.example.mybookshopapp.data.outher.litres;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
public class Instance {

    private int id;
    private String uuid;
    @JsonProperty("cover_url")
    private String coverUrl;
    private String url;
    private String title;
    private double cover_ratio;
    private boolean is_draft;
    private int art_type;
    //        private Prices prices;
    private boolean is_auto_speech_gift;
    private String subtitle;
    private int min_age;
    private boolean is_adult_content;
    private int cover_height;
    private int cover_width;
    private int foreign_publisher_id;
    private String language_code;
    private int symbols_count;
    private Object expected_symbols_count;
    private Object podcast_serial_number;
    private Date last_updated_at;
    private Date last_released_at;
    private int read_percent;
    private boolean is_finished;
    private boolean is_free;
    private boolean is_promo;
    private Object advertising_marking;
    private Object event_data;
    private int my_art_status;
    private boolean is_subscription_art;
    private boolean is_available_with_subscription;
    private String art_subscription_status_for_user;
    private boolean is_abonement_art;
    private boolean is_available_with_abonement;
    private boolean can_be_preordered;
    private boolean is_preorder_notified_for_user;
    private boolean is_exclusive_abonement;
    private boolean is_drm;
    private int in_gifts;
    private boolean is_fourth_art_gift;
    private Object podcast_left_to_buy;
    private int availability;
    private Date available_from;
    private Object library_information;
    private ArrayList<Person> persons;
    private boolean is_liked;
    //        private Rating rating;
    private ArrayList<Object> linked_arts;
    private ArrayList<Object> series;
    private String date_written_at;
    private int release_file_id;
    private int preview_file_id;
    private Labels labels;
    private Object in_folders;
    private boolean is_archived;
}
