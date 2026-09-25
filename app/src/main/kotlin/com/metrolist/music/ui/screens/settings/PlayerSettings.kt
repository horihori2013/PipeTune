/**
 * Metrolist Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.metrolist.music.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.metrolist.music.BuildConfig
import com.metrolist.music.LocalPlayerAwareWindowInsets
import com.metrolist.music.R
import com.metrolist.music.constants.AudioNormalizationKey
import com.metrolist.music.constants.AudioOffload
import com.metrolist.music.constants.AudioTrackPlaybackParamsKey
import com.metrolist.music.constants.AudioQuality
import com.metrolist.music.constants.AudioQualityKey
import com.metrolist.music.constants.AutoDownloadOnLikeKey
import com.metrolist.music.constants.CrossfadeDurationKey
import com.metrolist.music.constants.CrossfadeEnabledKey
import com.metrolist.music.constants.CrossfadeGaplessKey
import com.metrolist.music.constants.AutoLoadMoreKey
import com.metrolist.music.constants.AutoRadioQueueKey
import com.metrolist.music.constants.AutoSkipNextOnErrorKey
import com.metrolist.music.constants.AutoplayKey
import com.metrolist.music.constants.DisableLoadMoreWhenRepeatAllKey
import com.metrolist.music.constants.EnableGoogleCastKey
import com.metrolist.music.constants.HistoryDuration
import com.metrolist.music.constants.KeepScreenOn
import com.metrolist.music.constants.LoudnessLevel
import com.metrolist.music.constants.LoudnessLevelKey
import com.metrolist.music.constants.PauseOnMute
import com.metrolist.music.constants.PersistentQueueKey
import com.metrolist.music.constants.PersistentShuffleAcrossQueuesKey
import com.metrolist.music.constants.PreventDuplicateTracksInQueueKey
import com.metrolist.music.constants.RememberShuffleAndRepeatKey
import com.metrolist.music.constants.ResumeOnBluetoothConnectKey
import com.metrolist.music.constants.SeekExtraSeconds
import com.metrolist.music.constants.ShufflePlaylistFirstKey
import com.metrolist.music.constants.SimilarContent
import com.metrolist.music.constants.SkipSilenceInstantKey
import com.metrolist.music.constants.SkipSilenceKey
import com.metrolist.music.constants.StopMusicOnTaskClearKey
import com.metrolist.music.constants.VarispeedKey
import com.metrolist.music.ui.component.DefaultDialog
import com.metrolist.music.ui.component.EnumDialog
import com.metrolist.music.ui.component.IconButton
import com.metrolist.music.ui.component.Material3SettingsGroup
import com.metrolist.music.ui.component.Material3SettingsItem
import com.metrolist.music.ui.component.Material3SettingsToggle
import com.metrolist.music.ui.component.liquidglass.LiquidSwitch
import com.metrolist.music.ui.utils.backToMain
import com.metrolist.music.utils.rememberEnumPreference
import com.metrolist.music.utils.rememberPreference
import kotlin.math.roundToInt
import com.metrolist.music.ui.component.SleepTimerDialog
import com.metrolist.music.constants.SleepTimerEnabledKey
import com.metrolist.music.constants.SleepTimerRepeatKey
import com.metrolist.music.constants.SleepTimerCustomDaysKey
import com.metrolist.music.constants.SleepTimerEndTimeKey
import com.metrolist.music.constants.SleepTimerStartTimeKey
import com.metrolist.music.constants.SleepTimerDayTimesKey
import com.metrolist.music.ui.component.decodeDayTimes
import com.metrolist.music.ui.component.encodeDayTimes
import com.metrolist.music.constants.SleepTimerFadeOutKey
import com.metrolist.music.constants.SleepTimerStopAfterCurrentSongKey
import com.metrolist.music.ui.utils.getLoudnessLevelLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerSettings(
    navController: NavController
) {
    val (audioQuality, onAudioQualityChange) = rememberEnumPreference(
        AudioQualityKey,
        defaultValue = AudioQuality.AUTO
    )
    val (crossfadeEnabled, onCrossfadeEnabledChange) = rememberPreference(
        CrossfadeEnabledKey,
        defaultValue = false
    )
    val (crossfadeDuration, onCrossfadeDurationChange) = rememberPreference(
        CrossfadeDurationKey,
        defaultValue = 5f
    )
    val (crossfadeGapless, onCrossfadeGaplessChange) = rememberPreference(
        CrossfadeGaplessKey,
        defaultValue = true
    )
    val (persistentQueue, onPersistentQueueChange) = rememberPreference(
        PersistentQueueKey,
        defaultValue = true
    )
    val (skipSilence, onSkipSilenceChange) = rememberPreference(
        SkipSilenceKey,
        defaultValue = false
    )
    val (skipSilenceInstant, onSkipSilenceInstantChange) = rememberPreference(
        SkipSilenceInstantKey,
        defaultValue = false
    )
    val (audioNormalization, onAudioNormalizationChange) = rememberPreference(
        AudioNormalizationKey,
        defaultValue = true
    )

    val (loudnessLevel, onLoudnessLevelChange) = rememberEnumPreference(
        LoudnessLevelKey,
        defaultValue = LoudnessLevel.BALANCED,
    )

    val (audioOffload, onAudioOffloadChange) = rememberPreference(
        key = AudioOffload,
        defaultValue = false
    )

    val (audioTrackPlaybackParams, onAudioTrackPlaybackParamsChange) = rememberPreference(
        key = AudioTrackPlaybackParamsKey,
        defaultValue = true
    )

    val (varispeed, onVarispeedChange) = rememberPreference(
        key = VarispeedKey,
        defaultValue = false
    )

    val (enableGoogleCast, onEnableGoogleCastChange) = rememberPreference(
        key = EnableGoogleCastKey,
        defaultValue = true
    )

    val (seekExtraSeconds, onSeekExtraSeconds) = rememberPreference(
        SeekExtraSeconds,
        defaultValue = false
    )

    val (autoLoadMore, onAutoLoadMoreChange) = rememberPreference(
        AutoLoadMoreKey,
        defaultValue = true
    )
    val (autoRadioQueue, onAutoRadioQueueChange) = rememberPreference(
        AutoRadioQueueKey,
        defaultValue = true
    )
    val (disableLoadMoreWhenRepeatAll, onDisableLoadMoreWhenRepeatAllChange) = rememberPreference(
        DisableLoadMoreWhenRepeatAllKey,
        defaultValue = false
    )
    val (autoDownloadOnLike, onAutoDownloadOnLikeChange) = rememberPreference(
        AutoDownloadOnLikeKey,
        defaultValue = false
    )
    val (similarContentEnabled, similarContentEnabledChange) = rememberPreference(
        key = SimilarContent,
        defaultValue = true
    )
    val (autoSkipNextOnError, onAutoSkipNextOnErrorChange) = rememberPreference(
        AutoSkipNextOnErrorKey,
        defaultValue = false
    )
    val (autoplay, onAutoplayChange) = rememberPreference(
        AutoplayKey,
        defaultValue = true
    )
    val (persistentShuffleAcrossQueues, onPersistentShuffleAcrossQueuesChange) = rememberPreference(
        PersistentShuffleAcrossQueuesKey,
        defaultValue = false
    )
    val (rememberShuffleAndRepeat, onRememberShuffleAndRepeatChange) = rememberPreference(
        RememberShuffleAndRepeatKey,
        defaultValue = true
    )
    val (shufflePlaylistFirst, onShufflePlaylistFirstChange) = rememberPreference(
        ShufflePlaylistFirstKey,
        defaultValue = false
    )
    val (preventDuplicateTracksInQueue, onPreventDuplicateTracksInQueueChange) = rememberPreference(
        PreventDuplicateTracksInQueueKey,
        defaultValue = false
    )
    val (stopMusicOnTaskClear, onStopMusicOnTaskClearChange) = rememberPreference(
        StopMusicOnTaskClearKey,
        defaultValue = false
    )
    val (pauseOnMute, onPauseOnMuteChange) = rememberPreference(
        PauseOnMute,
        defaultValue = false
    )
    val (resumeOnBluetoothConnect, onResumeOnBluetoothConnectChange) = rememberPreference(
        ResumeOnBluetoothConnectKey,
        defaultValue = false
    )
    val (keepScreenOn, onKeepScreenOnChange) = rememberPreference(
        KeepScreenOn,
        defaultValue = false
    )
    val (historyDuration, onHistoryDurationChange) = rememberPreference(
        HistoryDuration,
        defaultValue = 30f
    )

    var showAudioQualityDialog by remember {
        mutableStateOf(false)
    }

    var showLoudnessLevelDialog by remember {
        mutableStateOf(false)
    }

    if (showAudioQualityDialog) {
        EnumDialog(
            onDismiss = { showAudioQualityDialog = false },
            onSelect = {
                onAudioQualityChange(it)
                showAudioQualityDialog = false
            },
            title = stringResource(R.string.audio_quality),
            current = audioQuality,
            values = AudioQuality.values().toList(),
            valueText = {
                when (it) {
                    AudioQuality.AUTO -> stringResource(R.string.audio_quality_auto)
                    AudioQuality.HIGH -> stringResource(R.string.audio_quality_high)
                    AudioQuality.LOW -> stringResource(R.string.audio_quality_low)
                }
            }
        )
    }

    if (showLoudnessLevelDialog) {
        EnumDialog(
            onDismiss = { showLoudnessLevelDialog = false },
            onSelect = {
                onLoudnessLevelChange(it)
                showLoudnessLevelDialog = false
            },
            title = stringResource(R.string.loudness_level),
            current = loudnessLevel,
            values = LoudnessLevel.values().toList(),
            valueText = { getLoudnessLevelLabel(it) }
        )
    }

    Column(
        Modifier
            .windowInsetsPadding(
                LocalPlayerAwareWindowInsets.current.only(
                    WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(
            Modifier.windowInsetsPadding(
                LocalPlayerAwareWindowInsets.current.only(
                    WindowInsetsSides.Top
                )
            )
        )

        Material3SettingsGroup(
            title = stringResource(R.string.player),
            items = buildList {
                add(Material3SettingsItem(
                    icon = painterResource(R.drawable.graphic_eq),
                    title = { Text(stringResource(R.string.audio_quality)) },
                    description = {
                        Text(
                            when (audioQuality) {
                                AudioQuality.AUTO -> stringResource(R.string.audio_quality_auto)
                                AudioQuality.HIGH -> stringResource(R.string.audio_quality_high)
                                AudioQuality.LOW -> stringResource(R.string.audio_quality_low)
                            }
                        )
                    },
                    onClick = { showAudioQualityDialog = true }
                ))
                add(Material3SettingsToggle(
                    icon = painterResource(R.drawable.linear_scale),
                    title = stringResource(R.string.crossfade),
                    description = stringResource(R.string.crossfade_desc),
                    checked = crossfadeEnabled,
                    onCheckedChange = onCrossfadeEnabledChange,
                ))
                if (crossfadeEnabled) {
                    add(Material3SettingsItem(
                        icon = painterResource(R.drawable.timer),
                        title = { Text(stringResource(R.string.crossfade_duration)) },
                        description = {
                            Column {
                                Text(pluralStringResource(R.plurals.seconds, crossfadeDuration.toInt(), crossfadeDuration.toInt()))
                                Slider(
                                    value = crossfadeDuration,
                                    onValueChange = onCrossfadeDurationChange,
                                    valueRange = 1f..15f,
                                    steps = 14
                                )
                            }
                        }
                    ))
                    add(Material3SettingsToggle(
                        icon = painterResource(R.drawable.album),
                        title = stringResource(R.string.crossfade_gapless),
                        description = stringResource(R.string.crossfade_gapless_desc),
                        checked = crossfadeGapless,
                        onCheckedChange = onCrossfadeGaplessChange,
                    ))
                }
                add(Material3SettingsItem(
                    icon = painterResource(R.drawable.history),
                    title = { Text(stringResource(R.string.history_duration)) },
                    description = {
                        Column {
                            Text(historyDuration.roundToInt().toString())
                            Slider(
                                value = historyDuration,
                                onValueChange = onHistoryDurationChange,
                                valueRange = 1f..100f
                            )
                        }
                    }
                ))
                add(Material3SettingsToggle(
                    icon = painterResource(R.drawable.fast_forward),
                    title = stringResource(R.string.skip_silence),
                    description = stringResource(R.string.skip_silence_desc),
                    checked = skipSilence,
                    onCheckedChange = onSkipSilenceChange,
                ))
                add(Material3SettingsItem(
                    icon = painterResource(R.drawable.skip_next),
                    title = { Text(stringResource(R.string.skip_silence_instant)) },
                    description = { Text(stringResource(R.string.skip_silence_instant_desc)) },
                    trailingContent = {
                        LiquidSwitch(
                            checked = skipSilenceInstant,
                            onCheckedChange = { onSkipSilenceInstantChange(it) },
                            enabled = skipSilence,
                        )
                    },
                    onClick = { if (skipSilence) onSkipSilenceInstantChange(!skipSilenceInstant) }
                ))
                add(Material3SettingsToggle(
                    icon = painterResource(R.drawable.volume_up),
                    title = stringResource(R.string.audio_normalization),
                    checked = audioNormalization,
                    onCheckedChange = onAudioNormalizationChange,
                ))
                if (audioNormalization) {
                    add(Material3SettingsItem(
                        icon = painterResource(R.drawable.volume_up),
                        title = { Text(stringResource(R.string.loudness_level)) },
                        description = {
                            Text(getLoudnessLevelLabel(loudnessLevel))
                        },
                        onClick = { showLoudnessLevelDialog = true }
                    ))
                }
                add(Material3SettingsItem(
                    icon = painterResource(R.drawable.graphic_eq),
                    title = { Text(stringResource(R.string.audio_offload)) },
                    description = {
                        Text(
                            if (crossfadeEnabled) stringResource(R.string.audio_offload_disabled_by_crossfade)
                            else stringResource(R.string.audio_offload_description)
                        )
                    },
                    trailingContent = {
                        LiquidSwitch(
                            checked = if (crossfadeEnabled) false else audioOffload,
                            onCheckedChange = onAudioOffloadChange,
                            enabled = !crossfadeEnabled,
                        )
                    },
                    onClick = { if (!crossfadeEnabled) onAudioOffloadChange(!audioOffload) }
                ))
                add(Material3SettingsToggle(
                    icon = painterResource(R.drawable.graphic_eq),
                    title = stringResource(R.string.varispeed),
                    description = stringResource(R.string.varispeed_description),
                    checked = varispeed,
                    onCheckedChange = onVarispeedChange,
                ))
                add(Material3SettingsToggle(
                    icon = painterResource(R.drawable.speed),
                    title = stringResource(R.string.audio_track_playback_params),
                    description = stringResource(R.string.audio_track_playback_params_description),
                    checked = audioTrackPlaybackParams,
                    onCheckedChange = onAudioTrackPlaybackParamsChange,
                ))
                // Only show Cast setting in GMS builds (not in F-Droid/FOSS)
                if (BuildConfig.CAST_AVAILABLE) {
                    add(Material3SettingsToggle(
                        icon = painterResource(R.drawable.cast),
                        title = stringResource(R.string.google_cast),
                        description = stringResource(R.string.google_cast_description),
                        checked = enableGoogleCast,
                        onCheckedChange = onEnableGoogleCastChange,
                    ))
                }
                add(Material3SettingsToggle(
                    icon = painterResource(R.drawable.arrow_forward),
                    title = stringResource(R.string.seek_seconds_addup),
                    description = stringResource(R.string.seek_seconds_addup_description),
                    checked = seekExtraSeconds,
                    onCheckedChange = onSeekExtraSeconds,
                ))
            }
        )

        Spacer(modifier = Modifier.height(27.dp))

        var showSleepTimerDialog by remember { mutableStateOf(false) }

        val (sleepTimerEnabled, onSleepTimerEnabledChange) = rememberPreference(
            SleepTimerEnabledKey,
            defaultValue = false
        )
        val (sleepTimerRepeat, onSleepTimerRepeatChange) = rememberPreference(
            SleepTimerRepeatKey,
            defaultValue = "daily"
        )
        val (sleepTimerStartTime, onSleepTimerStartTimeChange) = rememberPreference(
            SleepTimerStartTimeKey,
            defaultValue = "22:00"
        )
        val (sleepTimerEndTime, onSleepTimerEndTimeChange) = rememberPreference(
            SleepTimerEndTimeKey,
            defaultValue = "06:00"
        )
        val (sleepTimerCustomDays, onSleepTimerCustomDaysChange) = rememberPreference(
            SleepTimerCustomDaysKey,
            defaultValue = "0,1,2,3,4"
        )
        // Per-day time ranges used in custom mode
        val (sleepTimerDayTimes, onSleepTimerDayTimesChange) = rememberPreference(
            SleepTimerDayTimesKey,
            defaultValue = ""
        )

        val (sleepTimerStopAfterCurrentSong, onSleepTimerStopAfterCurrentSongChange) = rememberPreference (
        SleepTimerStopAfterCurrentSongKey,
        defaultValue = false)
        val (sleepTimerFadeOut, onSleepTimerFadeOutChange) = rememberPreference(
            SleepTimerFadeOutKey,
            false
        )

        if (showSleepTimerDialog) {
            val customDays = sleepTimerCustomDays.split(",").mapNotNull { it.toIntOrNull() }
            val dayTimesMap = decodeDayTimes(sleepTimerDayTimes)

            SleepTimerDialog(
                isVisible = true,
                onDismiss = { showSleepTimerDialog = false },
                onConfirm = { repeat, startTime, endTime, days, dayTimes ->
                    onSleepTimerRepeatChange(repeat)
                    onSleepTimerStartTimeChange(startTime)
                    onSleepTimerEndTimeChange(endTime)
                    onSleepTimerCustomDaysChange(days?.joinToString(",") ?: "0,1,2,3,4")
                    onSleepTimerDayTimesChange(encodeDayTimes(dayTimes))
                    showSleepTimerDialog = false
                },
                initialRepeat = sleepTimerRepeat,
                initialStartTime = sleepTimerStartTime,
                initialEndTime = sleepTimerEndTime,
                initialCustomDays = customDays,
                initialDayTimes = dayTimesMap
            )
        }

        Material3SettingsGroup(
            title = stringResource(R.string.sleep_timer),
            items = buildList {
                add(Material3SettingsToggle(
                    icon = painterResource(R.drawable.time_auto),
                    title = stringResource(R.string.enable_automatic_sleeptimer),
                    description = stringResource(R.string.sleeptimer_description),
                    checked = sleepTimerEnabled,
                    onCheckedChange = onSleepTimerEnabledChange,
                ))

                    add(
                        Material3SettingsItem(
                            icon = painterResource(R.drawable.baseline_event_repeat_24),
                            title = { Text(stringResource(R.string.sleep_timer_repeat)) },
                            description = {
                                Text(
                                    stringResource(R.string.sleep_timer_repeat_description)
                                )
                            },
                            trailingContent = {
                                LiquidSwitch(
                                    checked = sleepTimerEnabled,
                                    onCheckedChange = {showSleepTimerDialog = true},
                                )
                            },
                            onClick = { showSleepTimerDialog = true }
                        )
                    )


                add(Material3SettingsToggle(
                    icon = painterResource(R.drawable.more_time),
                    title = stringResource(R.string.sleep_timer_stop_after_current_song_title),
                    description = stringResource(R.string.sleep_timer_stop_after_current_song_description),
                    checked = sleepTimerStopAfterCurrentSong,
                    onCheckedChange = onSleepTimerStopAfterCurrentSongChange,
                ))

                add(Material3SettingsToggle(
                    icon = painterResource(R.drawable.timer_arrow_down),
                    title = stringResource(R.string.sleep_timer_fade_out_title),
                    description = stringResource(R.string.sleep_timer_fade_out_description),
                    checked = sleepTimerFadeOut,
                    onCheckedChange = onSleepTimerFadeOutChange,
                ))

            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        AlarmSettingsSection()

        Spacer(modifier = Modifier.height(27.dp))

        Material3SettingsGroup(
            title = stringResource(R.string.queue),
            items = listOf(
                Material3SettingsToggle(
                    icon = painterResource(R.drawable.queue_music),
                    title = stringResource(R.string.persistent_queue),
                    description = stringResource(R.string.persistent_queue_desc),
                    checked = persistentQueue,
                    onCheckedChange = onPersistentQueueChange,
                ),
                Material3SettingsToggle(
                    icon = painterResource(R.drawable.playlist_add),
                    title = stringResource(R.string.auto_load_more),
                    description = stringResource(R.string.auto_load_more_desc),
                    checked = autoLoadMore,
                    onCheckedChange = onAutoLoadMoreChange,
                ),
                Material3SettingsToggle(
                    icon = painterResource(R.drawable.radio),
                    title = stringResource(R.string.auto_radio_queue),
                    description = stringResource(R.string.auto_radio_queue_desc),
                    checked = autoRadioQueue,
                    onCheckedChange = onAutoRadioQueueChange,
                ),
                Material3SettingsToggle(
                    icon = painterResource(R.drawable.skip_next),
                    title = stringResource(R.string.autoplay),
                    description = stringResource(R.string.autoplay_desc),
                    checked = autoplay,
                    onCheckedChange = onAutoplayChange,
                ),
                Material3SettingsToggle(
                    icon = painterResource(R.drawable.repeat),
                    title = stringResource(R.string.disable_load_more_when_repeat_all),
                    description = stringResource(R.string.disable_load_more_when_repeat_all_desc),
                    checked = disableLoadMoreWhenRepeatAll,
                    onCheckedChange = onDisableLoadMoreWhenRepeatAllChange,
                ),
                Material3SettingsToggle(
                    icon = painterResource(R.drawable.download),
                    title = stringResource(R.string.auto_download_on_like),
                    description = stringResource(R.string.auto_download_on_like_desc),
                    checked = autoDownloadOnLike,
                    onCheckedChange = onAutoDownloadOnLikeChange,
                ),
                Material3SettingsToggle(
                    icon = painterResource(R.drawable.similar),
                    title = stringResource(R.string.enable_similar_content),
                    description = stringResource(R.string.similar_content_desc),
                    checked = similarContentEnabled,
                    onCheckedChange = similarContentEnabledChange,
                ),
                Material3SettingsToggle(
                    icon = painterResource(R.drawable.shuffle),
                    title = stringResource(R.string.persistent_shuffle_title),
                    description = stringResource(R.string.persistent_shuffle_desc),
                    checked = persistentShuffleAcrossQueues,
                    onCheckedChange = onPersistentShuffleAcrossQueuesChange,
                ),
                Material3SettingsToggle(
                    icon = painterResource(R.drawable.shuffle),
                    title = stringResource(R.string.remember_shuffle_and_repeat),
                    description = stringResource(R.string.remember_shuffle_and_repeat_desc),
                    checked = rememberShuffleAndRepeat,
                    onCheckedChange = onRememberShuffleAndRepeatChange,
                ),
                Material3SettingsToggle(
                    icon = painterResource(R.drawable.shuffle),
                    title = stringResource(R.string.shuffle_playlist_first),
                    description = stringResource(R.string.shuffle_playlist_first_desc),
                    checked = shufflePlaylistFirst,
                    onCheckedChange = onShufflePlaylistFirstChange,
                ),
                Material3SettingsToggle(
                    icon = painterResource(R.drawable.queue_music),
                    title = stringResource(R.string.prevent_duplicate_tracks_in_queue),
                    description = stringResource(R.string.prevent_duplicate_tracks_in_queue_desc),
                    checked = preventDuplicateTracksInQueue,
                    onCheckedChange = onPreventDuplicateTracksInQueueChange,
                ),
                Material3SettingsToggle(
                    icon = painterResource(R.drawable.skip_next),
                    title = stringResource(R.string.auto_skip_next_on_error),
                    description = stringResource(R.string.auto_skip_next_on_error_desc),
                    checked = autoSkipNextOnError,
                    onCheckedChange = onAutoSkipNextOnErrorChange,
                )
            )
        )

        Spacer(modifier = Modifier.height(27.dp))

        Material3SettingsGroup(
            title = stringResource(R.string.misc),
            items = listOf(
                Material3SettingsToggle(
                    icon = painterResource(R.drawable.clear_all),
                    title = stringResource(R.string.stop_music_on_task_clear),
                    checked = stopMusicOnTaskClear,
                    onCheckedChange = onStopMusicOnTaskClearChange,
                ),
                Material3SettingsToggle(
                    icon = painterResource(R.drawable.volume_off_pause),
                    title = stringResource(R.string.pause_music_when_media_is_muted),
                    checked = pauseOnMute,
                    onCheckedChange = onPauseOnMuteChange,
                ),
                Material3SettingsToggle(
                    icon = painterResource(R.drawable.bluetooth),
                    title = stringResource(R.string.resume_on_bluetooth_connect),
                    checked = resumeOnBluetoothConnect,
                    onCheckedChange = onResumeOnBluetoothConnectChange,
                ),
                Material3SettingsToggle(
                    icon = painterResource(R.drawable.screenshot),
                    title = stringResource(R.string.keep_screen_on_when_player_is_expanded),
                    checked = keepScreenOn,
                    onCheckedChange = onKeepScreenOnChange,
                )
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
    }

    TopAppBar(
        title = { Text(stringResource(R.string.player_and_audio)) },
        navigationIcon = {
            IconButton(
                onClick = navController::navigateUp,
                onLongClick = navController::backToMain
            ) {
                Icon(
                    painterResource(R.drawable.arrow_back),
                    contentDescription = null
                )
            }
        }
    )
}
