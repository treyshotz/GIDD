import { useForm, SubmitHandler } from 'react-hook-form';
import { ActivityHost } from 'types/Types';
import { useActivityHostsById, useAddActivityHost } from 'hooks/Activities';
import { useSnackbar } from 'hooks/Snackbar';
import { EMAIL_REGEX } from 'constant';

// Material-UI
import { makeStyles, Theme } from '@material-ui/core/styles';
import LinearProgress from '@material-ui/core/LinearProgress';
import Typography from '@material-ui/core/Typography';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';

// Project components
import Paper from 'components/layout/Paper';
import SubmitButton from 'components/inputs/SubmitButton';
import TextField from 'components/inputs/TextField';

const useStyles = makeStyles((theme: Theme) => ({
  list: {
    display: 'grid',
    gridGap: theme.spacing(1),
  },
  paper: {
    background: theme.palette.background.default,
  },
  secondaryText: {
    whiteSpace: 'break-spaces',
  },
}));

export type ActivityHostsProps = {
  activityId: string;
};

type FormValues = {
  email: string;
};

const ActivityHosts = ({ activityId }: ActivityHostsProps) => {
  const classes = useStyles();
  const { data: hosts, isLoading } = useActivityHostsById(activityId);
  const addHost = useAddActivityHost(activityId);
  const showSnackbar = useSnackbar();
  const { handleSubmit, register, formState, reset } = useForm<FormValues>();

  const submit: SubmitHandler<FormValues> = async (data) => {
    addHost.mutate(data.email, {
      onSuccess: () => {
        showSnackbar('Arrangøren ble lagt til', 'success');
        reset();
      },
      onError: (e) => {
        showSnackbar(e.detail, 'error');
      },
    });
  };

  type HostProps = {
    host: ActivityHost;
  };
  const Host = ({ host }: HostProps) => (
    <Paper className={classes.paper} noPadding>
      <ListItem>
        <ListItemText classes={{ secondary: classes.secondaryText }} primary={`${host.firstName} ${host.surname}`} secondary={`${host.email}`} />
      </ListItem>
    </Paper>
  );

  if (isLoading) {
    return <LinearProgress />;
  }

  return (
    <div className={classes.list}>
      {hosts?.map((host) => (
        <Host host={host} key={host.userId} />
      ))}
      <Paper className={classes.paper}>
        <Typography variant='h3'>Legg til arrangør</Typography>
        <form className={classes.list} onSubmit={handleSubmit(submit)}>
          <TextField
            formState={formState}
            label='Epost til ny arrangør'
            {...register('email', {
              required: 'Feltet er påkrevd',
              pattern: {
                value: EMAIL_REGEX,
                message: 'Ugyldig e-post',
              },
            })}
            required
          />
          <SubmitButton disabled={isLoading} formState={formState}>
            Legg til arrangør
          </SubmitButton>
        </form>
      </Paper>
    </div>
  );
};

export default ActivityHosts;