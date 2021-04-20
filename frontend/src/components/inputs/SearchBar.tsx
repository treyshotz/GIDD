import React from 'react';
import { makeStyles, Theme, createStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import InputBase from '@material-ui/core/InputBase';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import SearchIcon from '@material-ui/icons/Search';
import { useForm } from 'react-hook-form';
import { Activity } from 'types/Types';

// Material UI Components
import Popper, { PopperPlacementType } from '@material-ui/core/Popper';
import Fade from '@material-ui/core/Fade';
import DatePicker from './DatePicker';
import Bool from './Bool';
import { Button } from '@material-ui/core';

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      padding: '2px 4px',
      display: 'flex',
      alignItems: 'center',
      width: '100%',
      borderRadius: '20px',
    },
    input: {
      marginLeft: theme.spacing(1),
      flex: 1,
    },
    iconButton: {
      padding: 10,
    },
    divider: {
      height: 28,
      margin: 4,
    },
    paper: {
      border: '1px solid',
      padding: theme.spacing(1),
      marginTop: theme.spacing(1),
      borderRadius: theme.shape.borderRadius,
      width: '100%',
      backgroundColor: theme.palette.background.paper,
    },
    level: {
      display: 'flex',
      flexDirection: 'column',
    },
    rowTwo: {
      display: 'flex',
    },
    grid: {
      display: 'grid',
      gridTemplateColumns: '1fr 1fr',
      gap: theme.spacing(1),
    },
  }),
);

export default function SearchBar() {
  type FormValues = Pick<Activity, 'title' | 'description' | 'endDate' | 'startDate' | 'signupStart' | 'signupEnd' | 'capacity'>;

  const classes = useStyles();
  const [anchorEl, setAnchorEl] = React.useState<HTMLButtonElement | null>(null);
  const [open, setOpen] = React.useState(false);
  const [placement, setPlacement] = React.useState<PopperPlacementType>();
  const { control, formState } = useForm<FormValues>();

  const handleClick = (newPlacement: PopperPlacementType) => (event: React.MouseEvent<HTMLButtonElement>) => {
    setAnchorEl(event.currentTarget);
    setOpen((prev) => placement !== newPlacement || !prev);
    setPlacement(newPlacement);
  };
  const id = open ? 'transitions-popper' : undefined;

  return (
    <Paper className={classes.root} component='form'>
      <IconButton aria-label='menu' className={classes.iconButton} onClick={handleClick('bottom-end')}>
        <MenuIcon />
      </IconButton>
      <Popper anchorEl={anchorEl} id={id} open={open} placement={placement} transition>
        {({ TransitionProps }) => (
          <Fade {...TransitionProps} timeout={350}>
            <div className={classes.paper}>
              <div className={classes.grid}>
                <DatePicker control={control} formState={formState} label='Start' name='startDate' rules={{ required: 'Feltet er påkrevd' }} type='date-time' />
                <DatePicker control={control} formState={formState} label='Slutt' name='endDate' rules={{ required: 'Feltet er påkrevd' }} type='date-time' />
              </div>
              <div className={classes.rowTwo}>
                <div className={classes.level}>
                  <Bool control={control} formState={formState} label='Høy' name='level_high' type='checkbox' />
                  <Bool control={control} formState={formState} label='Middels' name='level_med' type='checkbox' />
                  <Bool control={control} formState={formState} label='Lav' name='level_low' type='checkbox' />
                </div>
              </div>
              <div className={classes.grid}>
                <Button>Nullstill Filtre</Button>
                <Button>Bruk Filtre</Button>
              </div>
            </div>
          </Fade>
        )}
      </Popper>
      <InputBase className={classes.input} inputProps={{ 'aria-label': 'search google maps' }} placeholder='Søk etter aktivitet' />
      <IconButton aria-label='search' className={classes.iconButton} type='submit'>
        <SearchIcon />
      </IconButton>
    </Paper>
  );
}
