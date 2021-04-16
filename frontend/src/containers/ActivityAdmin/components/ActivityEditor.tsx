import { useCallback, useEffect } from 'react';
import classnames from 'classnames';
import { useForm, SubmitHandler } from 'react-hook-form';
import { Activity } from 'types/Types';
import { useActivityById, useCreateActivity, useUpdateActivity, useDeleteActivity } from 'hooks/Activities';
import { useSnackbar } from 'hooks/Snackbar';
import { parseISO } from 'date-fns';

// Material-UI
import { makeStyles, Theme } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import LinearProgress from '@material-ui/core/LinearProgress';

// Project components
import DatePicker from 'components/inputs/DatePicker';
import VerifyDialog from 'components/layout/VerifyDialog';
import SubmitButton from 'components/inputs/SubmitButton';
import TextField from 'components/inputs/TextField';

const useStyles = makeStyles((theme: Theme) => ({
  grid: {
    display: 'grid',
    gridGap: theme.spacing(2),
    gridTemplateColumns: '1fr 1fr',
    [theme.breakpoints.down('sm')]: {
      gridGap: 0,
      gridTemplateColumns: '1fr',
    },
  },
  margin: {
    margin: theme.spacing(2, 0, 1),
    borderRadius: theme.shape.borderRadius,
    overflow: 'hidden',
  },
  red: {
    color: theme.palette.error.main,
    borderColor: theme.palette.error.main,
    '&:hover': {
      borderColor: theme.palette.error.light,
    },
  },
}));

export type ActivityEditorProps = {
  activityId: string | null;
  goToActivity: (newActivity: string | null) => void;
};

type FormValues = Pick<Activity, 'title' | 'description' | 'endDate' | 'location' | 'startDate' | 'signupStart' | 'signupEnd' | 'capacity'>;

const ActivityEditor = ({ activityId, goToActivity }: ActivityEditorProps) => {
  const classes = useStyles();
  const { data, isLoading } = useActivityById(activityId || '');
  const createActivity = useCreateActivity();
  const updateActivity = useUpdateActivity(activityId || '');
  const deleteActivity = useDeleteActivity(activityId || '');
  const showSnackbar = useSnackbar();
  const { control, handleSubmit, register, formState, setError, reset } = useForm<FormValues>();

  const setValues = useCallback(
    (newValues: Activity | null) => {
      reset({
        description: newValues?.description || '',
        endDate: newValues?.endDate.substring(0, 16) || new Date().toISOString().substring(0, 16),
        signupEnd: newValues?.signupEnd?.substring(0, 16) || new Date().toISOString().substring(0, 16),
        signupStart: newValues?.signupStart?.substring(0, 16) || new Date().toISOString().substring(0, 16),
        capacity: newValues?.capacity || 0,
        location: newValues?.location || '',
        startDate: newValues?.startDate.substring(0, 16) || new Date().toISOString().substring(0, 16),
        title: newValues?.title || '',
      });
    },
    [reset],
  );

  useEffect(() => {
    if (data) {
      setValues(data);
    } else {
      setValues(null);
    }
  }, [data, setValues]);

  const remove = async () => {
    deleteActivity.mutate(null, {
      onSuccess: (data) => {
        showSnackbar(data.detail, 'success');
        goToActivity(null);
      },
      onError: (e) => {
        showSnackbar(e.detail, 'error');
      },
    });
  };

  const cancelActivity = async () => {
    await updateActivity.mutate({ ...data, closed: true } as Activity, {
      onSuccess: () => {
        showSnackbar('Aktiviteten ble avlyst', 'success');
      },
      onError: (e) => {
        showSnackbar(e.detail, 'error');
      },
    });
  };

  const submit: SubmitHandler<FormValues> = async (data) => {
    if (parseISO(data.signupEnd) < parseISO(data.signupStart)) {
      setError('signupEnd', { message: 'Påmeldingsslutt må være etter påmeldingsstart' });
      return;
    }
    if (parseISO(data.startDate) < parseISO(data.signupEnd)) {
      setError('signupEnd', { message: 'Påmeldingsslutt må være før start på aktivitet' });
      return;
    }
    if (parseISO(data.endDate) < parseISO(data.startDate)) {
      setError('endDate', { message: 'Slutt på aktivitet må være etter start på aktivitet' });
      return;
    }
    if (activityId) {
      await updateActivity.mutate(data, {
        onSuccess: () => {
          showSnackbar('Aktiviteten ble oppdatert', 'success');
        },
        onError: (e) => {
          showSnackbar(e.detail, 'error');
        },
      });
    } else {
      await createActivity.mutate(data, {
        onSuccess: (newActivity) => {
          showSnackbar('Aktiviteten ble opprettet', 'success');
          goToActivity(newActivity.activityId);
        },
        onError: (e) => {
          showSnackbar(e.detail, 'error');
        },
      });
    }
  };

  if (isLoading) {
    return <LinearProgress />;
  }

  return (
    <>
      <form onSubmit={handleSubmit(submit)}>
        <Grid container direction='column' wrap='nowrap'>
          <div className={classes.grid}>
            <TextField formState={formState} label='Tittel' {...register('title', { required: 'Feltet er påkrevd' })} required />
            <TextField formState={formState} label='Sted' {...register('location')} />
          </div>
          <div className={classes.grid}>
            <DatePicker control={control} formState={formState} label='Start' name='startDate' rules={{ required: 'Feltet er påkrevd' }} type='date-time' />
            <DatePicker control={control} formState={formState} label='Slutt' name='endDate' rules={{ required: 'Feltet er påkrevd' }} type='date-time' />
          </div>
          <div className={classes.grid}>
            <DatePicker
              control={control}
              formState={formState}
              label='Start påmelding'
              name='signupStart'
              rules={{ required: 'Feltet er påkrevd' }}
              type='date-time'
            />
            <DatePicker
              control={control}
              formState={formState}
              label='Slutt påmelding'
              name='signupEnd'
              rules={{ required: 'Feltet er påkrevd' }}
              type='date-time'
            />
          </div>
          <div className={classes.grid}>
            <TextField
              formState={formState}
              inputProps={{ inputMode: 'numeric' }}
              label='Antall plasser'
              {...register('capacity', {
                pattern: { value: RegExp(/^[0-9]*$/), message: 'Skriv inn et heltall som 0 eller høyere' },
                valueAsNumber: true,
                min: { value: 0, message: 'Antall plasser må være 0 eller høyere' },
                required: 'Feltet er påkrevd',
              })}
              required
            />
          </div>
          <TextField
            formState={formState}
            label='Beskrivelse'
            maxRows={15}
            minRows={5}
            multiline
            {...register('description', { required: 'Gi arrengementet en beskrivelse' })}
            required
          />
          <SubmitButton className={classes.margin} disabled={isLoading} formState={formState}>
            {activityId ? 'Oppdater aktivitet' : 'Opprett aktivitet'}
          </SubmitButton>
          {Boolean(activityId) && (
            <div className={classes.grid}>
              <VerifyDialog
                className={classnames(classes.margin, classes.red)}
                contentText='Hvis du avlyser aktiviteten så er det ikke mulig å aktivere den igjen'
                onConfirm={cancelActivity}>
                Avlys aktivitet
              </VerifyDialog>
              <VerifyDialog
                className={classnames(classes.margin, classes.red)}
                contentText='Hvis du sletter aktiviteten så er det ikke mulig å gjenopprette den igjen'
                onConfirm={remove}>
                Slett aktivitet
              </VerifyDialog>
            </div>
          )}
        </Grid>
      </form>
    </>
  );
};

export default ActivityEditor;
