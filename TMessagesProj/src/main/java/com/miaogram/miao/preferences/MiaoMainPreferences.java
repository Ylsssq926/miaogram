/*
 * MiaoGram custom code.
 * Main settings page (the entry point users land on after tapping
 * "MiaoGram Settings" in Telegram's Profile/Me page).
 *
 * Hosts MiaoGram feature toggles, grouped into sections.
 * Built with a RecyclerListView + TextCheckCell, matching upstream settings pages.
 *
 * Adding a new toggle:
 *   1. Add a row-id field + assign it in buildRows().
 *   2. Map it to its Flag in flagForRow().
 *   3. Render its title/subtitle in onBindViewHolder.
 */
package com.miaogram.miao.preferences;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miaogram.miao.MiaoConfig;
import com.miaogram.miao.flags.Flag;
import com.miaogram.miao.flags.Flags;

import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class MiaoMainPreferences extends MiaoBasePreferencesEntry {

    // Row ids
    private int rowCount;
    private int brandHeaderRow;
    private int unifiedInboxNavRow;
    private int channelReaderNavRow;
    private int aboutNavRow;
    private int brandInfoRow;
    private int accountHeaderRow;
    private int maxAccountsRow;
    private int accountRemarkRow;
    private int accountInfoRow;
    private int folderHeaderRow;
    private int hideAllChatsRow;
    private int folderInfoRow;
    private int interfaceHeaderRow;
    private int hideStoriesRow;
    private int streamerModeRow;
    private int stripTrackingRow;
    private int showIdDcRow;
    private int messageSecondsRow;
    private int interfaceInfoRow;
    private int privacyHeaderRow;
    private int blockSponsoredRow;
    private int ghostModeRow;
    private int bypassRestrictionsRow;
    private int hideNonContactPhoneRow;
    private int safeDefaultsRow;
    private int privacyInfoRow;
    private int messagesHeaderRow;
    private int keywordMuteRow;
    private int keywordsEditRow;
    private int messagesInfoRow;
    private int translateHeaderRow;
    private int translateEnhancedRow;
    private int translateSourceRow;
    private int translateInfoRow;

    @NonNull
    @Override
    protected String getFragmentTitle() {
        return LocaleController.getString(R.string.MiaoSettings);
    }

    @Override
    protected View createBody(Context context) {
        buildRows();

        RecyclerListView listView = new RecyclerListView(context);
        listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        listView.setVerticalScrollBarEnabled(false);
        listView.setAdapter(new ListAdapter(context));
        listView.setOnItemClickListener((view, position) -> {
            Flag flag = flagForRow(position);
            if (flag != null && view instanceof TextCheckCell) {
                boolean newValue = !flag.isEnabled();
                MiaoConfig.setFeatureEnabled(flag, newValue);
                ((TextCheckCell) view).setChecked(newValue);
                onFlagToggled(flag);
                if (flag == Flags.MIAO_TR_1 && listView.getAdapter() != null) {
                    listView.getAdapter().notifyItemChanged(translateSourceRow);
                }
                return;
            }
            if (position == keywordsEditRow) {
                com.miaogram.miao.feature.KeywordFilterDialog.show(context, () -> {
                    if (listView.getAdapter() != null) {
                        listView.getAdapter().notifyItemChanged(keywordsEditRow);
                    }
                });
                return;
            }
            if (position == translateSourceRow) {
                showTranslateSourceDialog(context, () -> {
                    if (listView.getAdapter() != null) {
                        listView.getAdapter().notifyItemChanged(translateSourceRow);
                    }
                });
                return;
            }
            if (position == unifiedInboxNavRow) {
                presentFragment(new com.miaogram.miao.ui.unified.UnifiedInboxActivity());
                return;
            }
            if (position == channelReaderNavRow) {
                presentFragment(new com.miaogram.miao.ui.reader.ChannelReaderActivity());
                return;
            }
            if (position == aboutNavRow) {
                presentFragment(new com.miaogram.miao.ui.about.MiaoAboutActivity());
            }
        });

        FrameLayout container = new FrameLayout(context);
        container.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        return container;
    }

    /**
     * Applies side effects so toggles take effect without forcing the user to
     * restart or re-open the chat list, where the upstream consumer only reacts
     * to a NotificationCenter event.
     */
    private void onFlagToggled(Flag flag) {
        int account = org.telegram.messenger.UserConfig.selectedAccount;
        if (flag == Flags.MIAO_UI_3) {
            // Hide "All Chats" tab -> rebuild the folder tabs.
            org.telegram.messenger.NotificationCenter.getInstance(account)
                    .postNotificationName(org.telegram.messenger.NotificationCenter.dialogFiltersUpdated);
        } else if (flag == Flags.MIAO_UI_4) {
            // Hide Stories -> refresh the stories bar visibility.
            org.telegram.messenger.NotificationCenter.getInstance(account)
                    .postNotificationName(org.telegram.messenger.NotificationCenter.storiesUpdated);
        } else if (flag == Flags.MIAO_UI_6) {
            // Screenshot protection -> re-evaluate FLAG_SECURE immediately.
            if (org.telegram.ui.LaunchActivity.instance != null) {
                org.telegram.ui.LaunchActivity.instance.invalidateFlagSecure();
            }
        }
    }

    private void showTranslateSourceDialog(Context context, Runnable onChanged) {
        CharSequence[] options = new CharSequence[]{
                LocaleController.getString(R.string.MiaoTranslateSourceGoogle),
                LocaleController.getString(R.string.MiaoTranslateSourceTelegram),
        };
        org.telegram.ui.ActionBar.AlertDialog.Builder builder =
                new org.telegram.ui.ActionBar.AlertDialog.Builder(context);
        builder.setTitle(LocaleController.getString(R.string.MiaoTranslateSource));
        builder.setItems(options, (dialog, which) -> {
            String source = which == 1
                    ? com.miaogram.miao.feature.translate.MiaoTranslate.SOURCE_TELEGRAM
                    : com.miaogram.miao.feature.translate.MiaoTranslate.SOURCE_GOOGLE;
            com.miaogram.miao.feature.translate.MiaoTranslate.setSource(source);
            if (onChanged != null) {
                onChanged.run();
            }
        });
        builder.show();
    }

    private void buildRows() {
        rowCount = 0;
        brandHeaderRow = rowCount++;
        unifiedInboxNavRow = rowCount++;
        channelReaderNavRow = rowCount++;
        aboutNavRow = rowCount++;
        brandInfoRow = rowCount++;
        accountHeaderRow = rowCount++;
        maxAccountsRow = rowCount++;
        accountRemarkRow = rowCount++;
        accountInfoRow = rowCount++;
        folderHeaderRow = rowCount++;
        hideAllChatsRow = rowCount++;
        folderInfoRow = rowCount++;
        interfaceHeaderRow = rowCount++;
        hideStoriesRow = rowCount++;
        streamerModeRow = rowCount++;
        stripTrackingRow = rowCount++;
        showIdDcRow = rowCount++;
        messageSecondsRow = rowCount++;
        interfaceInfoRow = rowCount++;
        privacyHeaderRow = rowCount++;
        blockSponsoredRow = rowCount++;
        ghostModeRow = rowCount++;
        bypassRestrictionsRow = rowCount++;
        hideNonContactPhoneRow = rowCount++;
        safeDefaultsRow = rowCount++;
        privacyInfoRow = rowCount++;
        messagesHeaderRow = rowCount++;
        keywordMuteRow = rowCount++;
        keywordsEditRow = rowCount++;
        messagesInfoRow = rowCount++;
        translateHeaderRow = rowCount++;
        translateEnhancedRow = rowCount++;
        translateSourceRow = rowCount++;
        translateInfoRow = rowCount++;
    }

    /** Maps a toggle row to its backing Flag, or null for non-toggle rows. */
    @Nullable
    private Flag flagForRow(int position) {
        if (position == maxAccountsRow) return Flags.MIAO_AC_1;
        if (position == accountRemarkRow) return Flags.MIAO_AC_2;
        if (position == hideAllChatsRow) return Flags.MIAO_UI_3;
        if (position == hideStoriesRow) return Flags.MIAO_UI_4;
        if (position == streamerModeRow) return Flags.MIAO_UI_6;
        if (position == stripTrackingRow) return Flags.MIAO_UI_7;
        if (position == showIdDcRow) return Flags.MIAO_UI_8;
        if (position == messageSecondsRow) return Flags.MIAO_UI_9;
        if (position == blockSponsoredRow) return Flags.MIAO_PF_1;
        if (position == bypassRestrictionsRow) return Flags.MIAO_PF_2;
        if (position == ghostModeRow) return Flags.MIAO_PF_3;
        if (position == hideNonContactPhoneRow) return Flags.MIAO_PF_5;
        if (position == safeDefaultsRow) return Flags.MIAO_PF_6;
        if (position == keywordMuteRow) return Flags.MIAO_MS_1;
        if (position == translateEnhancedRow) return Flags.MIAO_TR_1;
        return null;
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {

        private static final int TYPE_CHECK = 0;
        private static final int TYPE_INFO = 1;
        private static final int TYPE_HEADER = 2;
        private static final int TYPE_SETTINGS = 3;
        private static final int TYPE_NAV = 4;

        private final Context context;

        ListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            int type = holder.getItemViewType();
            return type == TYPE_CHECK || type == TYPE_SETTINGS || type == TYPE_NAV;
        }

        @Override
        public int getItemCount() {
            return rowCount;
        }

        @Override
        public int getItemViewType(int position) {
            if (flagForRow(position) != null) {
                return TYPE_CHECK;
            }
            if (position == accountHeaderRow || position == folderHeaderRow
                    || position == interfaceHeaderRow || position == privacyHeaderRow
                    || position == messagesHeaderRow || position == brandHeaderRow
                    || position == translateHeaderRow) {
                return TYPE_HEADER;
            }
            if (position == unifiedInboxNavRow || position == channelReaderNavRow
                    || position == aboutNavRow) {
                return TYPE_NAV;
            }
            if (position == keywordsEditRow || position == translateSourceRow) {
                return TYPE_SETTINGS;
            }
            return TYPE_INFO;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            if (viewType == TYPE_CHECK) {
                view = new TextCheckCell(context);
                view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            } else if (viewType == TYPE_HEADER) {
                view = new HeaderCell(context);
                view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            } else if (viewType == TYPE_SETTINGS) {
                view = new TextSettingsCell(context);
                view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            } else if (viewType == TYPE_NAV) {
                view = new TextCell(context);
                view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            } else {
                view = new TextInfoPrivacyCell(context);
            }
            return new RecyclerListView.Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case TYPE_CHECK: {
                    TextCheckCell cell = (TextCheckCell) holder.itemView;
                    if (position == maxAccountsRow) {
                        cell.setTextAndValueAndCheck(
                                LocaleController.getString(R.string.MiaoMaxAccounts),
                                LocaleController.getString(R.string.MiaoMaxAccountsInfo),
                                Flags.MIAO_AC_1.isEnabled(), true, true);
                    } else if (position == accountRemarkRow) {
                        cell.setTextAndValueAndCheck(
                                LocaleController.getString(R.string.MiaoAccountRemark),
                                LocaleController.getString(R.string.MiaoAccountRemarkInfo),
                                Flags.MIAO_AC_2.isEnabled(), true, false);
                    } else if (position == hideAllChatsRow) {
                        cell.setTextAndValueAndCheck(
                                LocaleController.getString(R.string.MiaoHideAllChats),
                                LocaleController.getString(R.string.MiaoHideAllChatsInfo),
                                Flags.MIAO_UI_3.isEnabled(), true, false);
                    } else if (position == hideStoriesRow) {
                        cell.setTextAndValueAndCheck(
                                LocaleController.getString(R.string.MiaoHideStories),
                                LocaleController.getString(R.string.MiaoHideStoriesInfo),
                                Flags.MIAO_UI_4.isEnabled(), true, true);
                    } else if (position == streamerModeRow) {
                        cell.setTextAndValueAndCheck(
                                LocaleController.getString(R.string.MiaoStreamerMode),
                                LocaleController.getString(R.string.MiaoStreamerModeInfo),
                                Flags.MIAO_UI_6.isEnabled(), true, true);
                    } else if (position == stripTrackingRow) {
                        cell.setTextAndValueAndCheck(
                                LocaleController.getString(R.string.MiaoStripTracking),
                                LocaleController.getString(R.string.MiaoStripTrackingInfo),
                                Flags.MIAO_UI_7.isEnabled(), true, true);
                    } else if (position == showIdDcRow) {
                        cell.setTextAndValueAndCheck(
                                LocaleController.getString(R.string.MiaoShowIdDc),
                                LocaleController.getString(R.string.MiaoShowIdDcInfo),
                                Flags.MIAO_UI_8.isEnabled(), true, true);
                    } else if (position == messageSecondsRow) {
                        cell.setTextAndValueAndCheck(
                                LocaleController.getString(R.string.MiaoMessageSeconds),
                                LocaleController.getString(R.string.MiaoMessageSecondsInfo),
                                Flags.MIAO_UI_9.isEnabled(), true, false);
                    } else if (position == blockSponsoredRow) {
                        cell.setTextAndValueAndCheck(
                                LocaleController.getString(R.string.MiaoBlockSponsored),
                                LocaleController.getString(R.string.MiaoBlockSponsoredInfo),
                                Flags.MIAO_PF_1.isEnabled(), true, true);
                    } else if (position == ghostModeRow) {
                        cell.setTextAndValueAndCheck(
                                LocaleController.getString(R.string.MiaoGhostMode),
                                LocaleController.getString(R.string.MiaoGhostModeInfo),
                                Flags.MIAO_PF_3.isEnabled(), true, true);
                    } else if (position == bypassRestrictionsRow) {
                        cell.setTextAndValueAndCheck(
                                LocaleController.getString(R.string.MiaoBypassRestrictions),
                                LocaleController.getString(R.string.MiaoBypassRestrictionsInfo),
                                Flags.MIAO_PF_2.isEnabled(), true, true);
                    } else if (position == hideNonContactPhoneRow) {
                        cell.setTextAndValueAndCheck(
                                LocaleController.getString(R.string.MiaoHideNonContactPhone),
                                LocaleController.getString(R.string.MiaoHideNonContactPhoneInfo),
                                Flags.MIAO_PF_5.isEnabled(), true, true);
                    } else if (position == safeDefaultsRow) {
                        cell.setTextAndValueAndCheck(
                                LocaleController.getString(R.string.MiaoSafeDefaults),
                                LocaleController.getString(R.string.MiaoSafeDefaultsInfo),
                                Flags.MIAO_PF_6.isEnabled(), true, false);
                    } else if (position == keywordMuteRow) {
                        cell.setTextAndValueAndCheck(
                                LocaleController.getString(R.string.MiaoKeywordMute),
                                LocaleController.getString(R.string.MiaoKeywordMuteInfo),
                                Flags.MIAO_MS_1.isEnabled(), true, true);
                    } else if (position == translateEnhancedRow) {
                        cell.setTextAndValueAndCheck(
                                LocaleController.getString(R.string.MiaoTranslateEnhanced),
                                LocaleController.getString(R.string.MiaoTranslateEnhancedInfo),
                                Flags.MIAO_TR_1.isEnabled(), true, true);
                    }
                    break;
                }
                case TYPE_HEADER: {
                    HeaderCell cell = (HeaderCell) holder.itemView;
                    if (position == brandHeaderRow) {
                        cell.setText(LocaleController.getString(R.string.MiaoFeaturesSectionInfo));
                    } else if (position == accountHeaderRow) {
                        cell.setText(LocaleController.getString(R.string.MiaoAccountSectionInfo));
                    } else if (position == folderHeaderRow) {
                        cell.setText(LocaleController.getString(R.string.MiaoFolderSectionInfo));
                    } else if (position == interfaceHeaderRow) {
                        cell.setText(LocaleController.getString(R.string.MiaoInterfaceSectionInfo));
                    } else if (position == privacyHeaderRow) {
                        cell.setText(LocaleController.getString(R.string.MiaoPrivacySectionInfo));
                    } else if (position == messagesHeaderRow) {
                        cell.setText(LocaleController.getString(R.string.MiaoMessagesSectionInfo));
                    } else if (position == translateHeaderRow) {
                        cell.setText(LocaleController.getString(R.string.MiaoTranslateSection));
                    }
                    break;
                }
                case TYPE_NAV: {
                    TextCell cell = (TextCell) holder.itemView;
                    if (position == unifiedInboxNavRow) {
                        cell.setTextAndValueAndIcon(
                                LocaleController.getString(R.string.MiaoUnifiedInbox),
                                LocaleController.getString(R.string.MiaoUnifiedInboxEntryInfo),
                                R.drawable.msg_discussion, true);
                    } else if (position == channelReaderNavRow) {
                        cell.setTextAndValueAndIcon(
                                LocaleController.getString(R.string.MiaoChannelReaderEntry),
                                LocaleController.getString(R.string.MiaoChannelReaderEntryInfo),
                                R.drawable.msg_folders, true);
                    } else if (position == aboutNavRow) {
                        cell.setTextAndIcon(
                                LocaleController.getString(R.string.MiaoAbout),
                                R.drawable.msg_info, false);
                    }
                    break;
                }
                case TYPE_SETTINGS: {
                    TextSettingsCell cell = (TextSettingsCell) holder.itemView;
                    if (position == keywordsEditRow) {
                        int n = com.miaogram.miao.feature.KeywordFilter.count();
                        String value = n == 0
                                ? LocaleController.getString(R.string.MiaoKeywordsNone)
                                : LocaleController.formatPluralString("MiaoKeywordsCount", n);
                        cell.setTextAndValue(
                                LocaleController.getString(R.string.MiaoKeywords),
                                value, true);
                    } else if (position == translateSourceRow) {
                        String source = com.miaogram.miao.feature.translate.MiaoTranslate.getSource();
                        String value = com.miaogram.miao.feature.translate.MiaoTranslate.SOURCE_TELEGRAM.equals(source)
                                ? LocaleController.getString(R.string.MiaoTranslateSourceTelegram)
                                : LocaleController.getString(R.string.MiaoTranslateSourceGoogle);
                        cell.setTextAndValue(
                                LocaleController.getString(R.string.MiaoTranslateSource),
                                value, false);
                        cell.setEnabled(Flags.MIAO_TR_1.isEnabled(), null);
                    }
                    break;
                }
                default: {
                    TextInfoPrivacyCell cell = (TextInfoPrivacyCell) holder.itemView;
                    if (position == brandInfoRow) {
                        cell.setText(LocaleController.getString(R.string.MiaoFeaturesSectionHint));
                    } else if (position == accountInfoRow) {
                        cell.setText(LocaleController.getString(R.string.MiaoMaxAccountsHint));
                    } else if (position == folderInfoRow) {
                        cell.setText(LocaleController.getString(R.string.MiaoHideAllChatsHint));
                    } else if (position == interfaceInfoRow) {
                        cell.setText(LocaleController.getString(R.string.MiaoInterfaceSectionHint));
                    } else if (position == privacyInfoRow) {
                        cell.setText(LocaleController.getString(R.string.MiaoPrivacySectionHint));
                    } else if (position == messagesInfoRow) {
                        cell.setText(LocaleController.getString(R.string.MiaoMessagesSectionHint));
                    } else if (position == translateInfoRow) {
                        cell.setText(LocaleController.getString(R.string.MiaoTranslateSectionHint));
                    }
                    break;
                }
            }
        }
    }
}
