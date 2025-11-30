package com.bleudev.ppl_utils.feature.chatfilter;

import com.bleudev.ppl_utils.PplUtilsConst;
import com.bleudev.ppl_utils.config.PplUtilsConfig;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

/**
 * Manages chat message filtering based on whitelist/blacklist mode.
 */
public class ChatFilter {
    public enum FilterMode {
        WHITELIST,
        BLACKLIST
    }
    
    /**
     * Checks if a message should be displayed based on current filter settings.
     * @param message The chat message text
     * @return true if message should be displayed, false if it should be hidden
     */
    public static boolean shouldDisplayMessage(@NotNull Text message) {
        // If filter is disabled, show all messages
        if (!PplUtilsConfig.chat_filter_enabled) {
            return true;
        }
        
        String messageText = message.getString().toLowerCase(Locale.ROOT);
        List<String> filterWords = PplUtilsConfig.chat_filter_words;
        
        if (filterWords == null || filterWords.isEmpty()) {
            // If no words in filter, whitelist mode shows nothing, blacklist shows everything
            return PplUtilsConfig.chat_filter_mode == PplUtilsConfig.ChatFilterMode.BLACKLIST;
        }
        
        // Check if message contains any filter word
        boolean containsFilterWord = filterWords.stream()
            .anyMatch(word -> {
                String lowerWord = word.toLowerCase(Locale.ROOT);
                return messageText.contains(lowerWord);
            });
        
        if (PplUtilsConfig.chat_filter_mode == PplUtilsConfig.ChatFilterMode.WHITELIST) {
            // Whitelist: only show messages containing filter words
            return containsFilterWord;
        } else {
            // Blacklist: hide messages containing filter words
            return !containsFilterWord;
        }
    }
    
    /**
     * Gets the current filter mode.
     */
    @NotNull
    public static FilterMode getMode() {
        if (PplUtilsConfig.chat_filter_mode == null) {
            return FilterMode.BLACKLIST;
        }
        return PplUtilsConfig.chat_filter_mode == PplUtilsConfig.ChatFilterMode.WHITELIST 
            ? FilterMode.WHITELIST 
            : FilterMode.BLACKLIST;
    }
    
    /**
     * Sets the filter mode.
     */
    public static void setMode(@NotNull FilterMode mode) {
        PplUtilsConfig.chat_filter_mode = mode == FilterMode.WHITELIST 
            ? PplUtilsConfig.ChatFilterMode.WHITELIST 
            : PplUtilsConfig.ChatFilterMode.BLACKLIST;
        PplUtilsConfig.saveConfig();
    }
    
    /**
     * Checks if filter is enabled.
     */
    public static boolean isEnabled() {
        return PplUtilsConfig.chat_filter_enabled;
    }
    
    /**
     * Enables or disables the filter.
     */
    public static void setEnabled(boolean enabled) {
        PplUtilsConfig.chat_filter_enabled = enabled;
        PplUtilsConfig.saveConfig();
    }
    
    /**
     * Adds a word to the filter list.
     * Automatically enables the filter if it was disabled.
     */
    public static boolean addWord(@NotNull String word) {
        if (PplUtilsConfig.chat_filter_words == null) {
            PplUtilsConfig.chat_filter_words = new java.util.ArrayList<>();
        }
        
        String lowerWord = word.toLowerCase(Locale.ROOT).trim();
        if (lowerWord.isEmpty()) {
            return false;
        }
        
        if (!PplUtilsConfig.chat_filter_words.contains(lowerWord)) {
            PplUtilsConfig.chat_filter_words.add(lowerWord);
            // Automatically enable filter when adding first word
            if (!PplUtilsConfig.chat_filter_enabled) {
                PplUtilsConfig.chat_filter_enabled = true;
                PplUtilsConst.LOGGER.info("Chat filter automatically enabled after adding word: {}", lowerWord);
            }
            PplUtilsConfig.saveConfig();
            return true;
        }
        return false;
    }
    
    /**
     * Removes a word from the filter list.
     */
    public static boolean removeWord(@NotNull String word) {
        if (PplUtilsConfig.chat_filter_words == null) {
            return false;
        }
        
        String lowerWord = word.toLowerCase(Locale.ROOT).trim();
        boolean removed = PplUtilsConfig.chat_filter_words.remove(lowerWord);
        if (removed) {
            PplUtilsConfig.saveConfig();
        }
        return removed;
    }
    
    /**
     * Gets the list of filter words.
     */
    @NotNull
    public static List<String> getWords() {
        if (PplUtilsConfig.chat_filter_words == null) {
            return new java.util.ArrayList<>();
        }
        return new java.util.ArrayList<>(PplUtilsConfig.chat_filter_words);
    }
}

