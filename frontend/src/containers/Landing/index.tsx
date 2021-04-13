import Helmet from 'react-helmet';

// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';

// Project Components
import Navigation from 'components/navigation/Navigation';

const useStyles = makeStyles((theme) => ({
  header: {
    marginTop: theme.spacing(1),
  },
}));

const Landing = () => {
  const classes = useStyles();

  return (
    <Navigation>
      <Helmet>
        <title>Forsiden - Gidd</title>
      </Helmet>
      <Typography align='center' className={classes.header} color='inherit' gutterBottom variant='h2'>
        Gidd
      </Typography>
      <Typography align='center' variant='body1'>
        Det er bare å gidde
      </Typography>
    </Navigation>
  );
};

export default Landing;
