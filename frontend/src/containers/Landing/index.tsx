import Helmet from 'react-helmet';

// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import { Typography, Button, Container } from '@material-ui/core';

// Project Components
import Navigation from 'components/navigation/Navigation';
import image from 'assets/img/DefaultBackground.jpg';
// import ActivityCard from 'components/layout/ActivityCard';

const useStyles = makeStyles((theme) => ({
  header: {
    marginTop: theme.spacing(1),
  },
  img: {
    background: `${theme.palette.colors.gradient}, url(${image}) center center/cover no-repeat scroll`,
    height: '100vh',
    width: '100%',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    boxShadow: 'inset 0 0 0 1000px rgba(0, 0, 0, 0.2)',
    objectFit: 'contain',
  },
  activityContainer: {
    textAlign: 'center',
  },
  activities: {
    display: 'flex',
    flexWrap: 'wrap',
    justifyContent: 'space-around',
    margin: 'auto',
  },
  btnGroup: {
    display: 'flex',
    gap: theme.spacing(1),
  },
}));

const Landing = () => {
  const classes = useStyles();

  return (
    <Navigation maxWidth={false}>
      <Helmet>
        <title>Forsiden - Gidd</title>
      </Helmet>
      <div className={classes.img}>
        <Typography align='center' className={classes.header} color='inherit' gutterBottom variant='h2'>
          Gidd
        </Typography>
        <Typography align='center' variant='body1'>
          Det er bare å gidde
        </Typography>
        <div className={classes.btnGroup}>
          <Button variant='outlined'>Logg inn</Button>
          <Button variant='outlined'>Registrer deg</Button>
        </div>
      </div>
      <Container className={classes.activityContainer}>
        <Typography variant='h1'>Nye Aktiviteter</Typography>
        <div className={classes.activities}>{/*<ActivityCard /> */}</div>
      </Container>
    </Navigation>
  );
};

export default Landing;
