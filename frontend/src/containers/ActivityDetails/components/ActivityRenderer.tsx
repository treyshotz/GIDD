import { useState } from 'react';
import classnames from 'classnames';
import { Link } from 'react-router-dom';
import { Activity } from 'types/Types';
import URLS from 'URLS';
import { parseISO, isPast, isFuture } from 'date-fns';
import { formatDate } from 'utils';

// Services
import { useActivityRegistration, useDeleteActivityRegistration } from 'hooks/Activities';
import { useUser } from 'hooks/User';
import { useSnackbar } from 'hooks/Snackbar';

// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import Skeleton from '@material-ui/core/Skeleton';
import Alert from '@material-ui/core/Alert';
import SwipeableDrawer from '@material-ui/core/SwipeableDrawer';

// Project Components
import ActivityRegistration from 'containers/ActivityDetails/components/ActivityRegistration';
import Paper from 'components/layout/Paper';
import VerifyDialog from 'components/layout/VerifyDialog';
import MasonryGrid from 'components/layout/MasonryGrid';

// Google Maps Components
import { GoogleMap, LoadScript, Marker } from '@react-google-maps/api';
import { GOOGLE_MAPS_API_KEY } from 'constant';

const useStyles = makeStyles((theme) => ({
  image: {
    borderRadius: theme.shape.borderRadius,
    border: `1px solid ${theme.palette.divider}`,
  },
  rootGrid: {
    display: 'grid',
    gridTemplateColumns: '4fr 3fr',
    gridGap: theme.spacing(2),
    position: 'relative',
    alignItems: 'self-start',
    [theme.breakpoints.down('md')]: {
      gridGap: theme.spacing(1),
      gridTemplateColumns: '1fr',
    },
  },
  grid: {
    display: 'grid',
    gridGap: theme.spacing(2),
    alignItems: 'self-start',
    [theme.breakpoints.down('md')]: {
      gridGap: theme.spacing(1),
    },
  },
  content: {
    minHeight: 'calc(100vh - 100px)',
    gridTemplateRows: '1fr auto',
    [theme.breakpoints.down('md')]: {
      minHeight: 'unset',
      gridTemplateRows: 'auto',
    },
  },
  images: {},
  details: {
    padding: theme.spacing(1, 2),
    width: '100%',
  },
  detailsHeader: {
    fontSize: '1.5rem',
  },
  alert: {
    marginBottom: theme.spacing(1),
  },
  title: {
    color: theme.palette.text.primary,
    fontSize: '2.4rem',
    wordWrap: 'break-word',
  },
  description: {
    whiteSpace: 'break-spaces',
  },
  applyButton: {
    height: 50,
    fontWeight: 'bold',
  },
  skeleton: {
    maxWidth: '100%',
    borderRadius: theme.shape.borderRadius,
  },
  signupPaper: {
    maxWidth: theme.breakpoints.values.md,
    margin: 'auto',
    padding: theme.spacing(3, 2, 5),
    borderRadius: `${theme.shape.borderRadius}px ${theme.shape.borderRadius}px 0 0`,
    background: theme.palette.background.paper,
  },
  img: {
    width: '100%',
    borderRadius: theme.shape.borderRadius,
  },
  containerStyle: {
    width: '100%',
    height: 400,
    borderRadius: theme.shape.borderRadius,
    [theme.breakpoints.down('md')]: {
      height: 300,
    },
  },
}));

export type ActivityRendererProps = {
  data: Activity;
  preview?: boolean;
};

const ActivityRenderer = ({ data, preview = false }: ActivityRendererProps) => {
  const classes = useStyles();
  const { data: user } = useUser();
  const { data: registration } = useActivityRegistration(data.id, preview || !user ? '' : user.id);
  const deleteRegistration = useDeleteActivityRegistration(data.id);
  const showSnackbar = useSnackbar();
  const [viewSignup, setViewSignup] = useState(false);
  const startDate = parseISO(data.startDate);
  const endDate = parseISO(data.endDate);
  const signupStart = parseISO(data.signupStart);
  const signupEnd = parseISO(data.signupEnd);

  const signOff = async () => {
    if (user) {
      deleteRegistration.mutate(user.id, {
        onSuccess: (data) => {
          showSnackbar(data.message, 'success');
          setViewSignup(false);
        },
        onError: (e) => {
          showSnackbar(e.message, 'error');
        },
      });
    }
  };

  const ApplyButton = () => {
    if (preview) {
      return null;
    } else if (data.closed) {
      return (
        <Alert className={classes.details} severity='warning' variant='outlined'>
          Denne aktiviteten er avlyst. Det er derfor ikke mulig å melde seg på.
        </Alert>
      );
    } else if (!user) {
      if (isFuture(signupEnd)) {
        return (
          <Button className={classes.applyButton} component={Link} fullWidth to={URLS.LOGIN}>
            Logg inn for å melde deg på
          </Button>
        );
      } else {
        return null;
      }
    } else if (registration) {
      return (
        <>
          <Alert className={classes.details} severity='success' variant='outlined'>
            Du har plass på aktiviteten!
          </Alert>
          {isFuture(startDate) ? (
            <VerifyDialog className={classes.applyButton} onConfirm={signOff} titleText='Meld deg av'>
              Meld deg av
            </VerifyDialog>
          ) : (
            <Alert className={classes.details} severity='info' variant='outlined'>
              Avmeldingsfristen er passert
            </Alert>
          )}
        </>
      );
    } else if (isFuture(signupStart)) {
      return (
        <Button className={classes.applyButton} color='primary' disabled fullWidth variant='contained'>
          Påmelding har ikke startet
        </Button>
      );
    } else if (isPast(signupEnd)) {
      return null;
    } else {
      return (
        <Button className={classes.applyButton} color='primary' fullWidth onClick={() => setViewSignup(true)} variant='contained'>
          Meld deg på
        </Button>
      );
    }
  };

  return (
    <div className={classes.rootGrid}>
      <div className={classnames(classes.grid, classes.content)}>
        <div>
          <Typography className={classes.title} gutterBottom variant='h1'>
            {data.title}
          </Typography>
          <Typography className={classes.description}>{data.description}</Typography>
        </div>
        <div className={classes.grid}>
          <Paper>
            <Typography className={classes.detailsHeader} variant='h2'>
              Detaljer
            </Typography>
            <Typography>{`Fra: ${formatDate(startDate)}`}</Typography>
            <Typography>{`Til: ${formatDate(endDate)}`}</Typography>
            <Typography>{`Start påmelding: ${formatDate(signupStart)}`}</Typography>
            <Typography>{`Slutt påmelding: ${formatDate(signupEnd)}`}</Typography>
            <Typography>{`Påmeldte: 0/${data.capacity}`}</Typography>
          </Paper>
          <ApplyButton />
          <SwipeableDrawer
            anchor='bottom'
            classes={{ paper: classes.signupPaper }}
            disableSwipeToOpen
            onClose={() => setViewSignup(false)}
            onOpen={() => setViewSignup(true)}
            open={viewSignup}
            swipeAreaWidth={56}>
            {user && <ActivityRegistration activity={data} closeDialog={() => setViewSignup(false)} user={user} />}
          </SwipeableDrawer>
          {!preview && (data.hosts.some((host) => host.id === user?.id) || data.creator.id === user?.id) && (
            <Button component={Link} fullWidth to={`${URLS.ADMIN_ACTIVITIES}${data.id}/`} variant='outlined'>
              Endre aktivitet
            </Button>
          )}
        </div>
        <div className={classes.grid}>
          <LoadScript googleMapsApiKey={GOOGLE_MAPS_API_KEY}>
            <GoogleMap center={data.geoLocation} mapContainerClassName={classes.containerStyle} zoom={14}>
              <Marker position={data.geoLocation} />
            </GoogleMap>
          </LoadScript>
        </div>
      </div>
      <div className={classnames(classes.grid, classes.images)}>
        <MasonryGrid
          breakpoints={{
            default: 2,
            900: 1,
          }}>
          {data.images.map((image, i) => (
            <img className={classes.img} key={i} src={image.url} />
          ))}
        </MasonryGrid>
      </div>
    </div>
  );
};

export default ActivityRenderer;

export const ActivityRendererLoading = () => {
  const classes = useStyles();

  return (
    <div className={classes.rootGrid}>
      <div className={classnames(classes.grid, classes.content)}>
        <div>
          <Skeleton className={classes.skeleton} height={80} width='60%' />
          <Skeleton className={classes.skeleton} height={40} width={250} />
          <Skeleton className={classes.skeleton} height={40} width='80%' />
          <Skeleton className={classes.skeleton} height={40} width='85%' />
          <Skeleton className={classes.skeleton} height={40} width='75%' />
          <Skeleton className={classes.skeleton} height={40} width='90%' />
        </div>
        <div className={classes.grid}>
          <Paper>
            <Skeleton className={classes.skeleton} height={80} width='60%' />
            <Skeleton className={classes.skeleton} height={40} width={250} />
            <Skeleton className={classes.skeleton} height={40} width='80%' />
            <Skeleton className={classes.skeleton} height={40} width='85%' />
          </Paper>
        </div>
      </div>
      <div className={classnames(classes.grid, classes.images)}>
        <MasonryGrid
          breakpoints={{
            default: 2,
            900: 1,
          }}>
          <Skeleton className={classes.img} height={150} />
          <Skeleton className={classes.img} height={200} />
          <Skeleton className={classes.img} height={180} />
          <Skeleton className={classes.img} height={120} />
        </MasonryGrid>
      </div>
    </div>
  );
};
